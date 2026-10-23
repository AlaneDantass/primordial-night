package net.poo.primordialnight.entity.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.poo.primordialnight.entity.ai.goal.TeleportBehindTargetGoal;
import net.poo.primordialnight.entity.ai.goal.YmaPytuAttackGoal;
import net.poo.primordialnight.entity.enums.BossPhase;

/**
 * Main Boss Entity: Yma-Pytu, The First Night (Titanic Void Golem of The End).
 *
 * Responsible: Dev 1 (Entity Architecture, Lifecycle, Boss Bar & Phase State Machine)
 *
 * OOP Concepts Applied:
 * - Inheritance: extends Monster (reuses pathfinding, health, combat, and collision logic)
 * - Polymorphism (@Override): registerGoals(), customServerAiStep(), startSeenByPlayer(),
 *   stopSeenByPlayer(), spawnAtLocation(), tick()
 * - Encapsulation: private final ServerBossEvent and SynchedEntityData accessors
 * - State Machine: transitions from BossPhase.PHASE_ONE to BossPhase.ENRAGED below 50% HP
 *
 * Reference Repositories:
 * - mirror-abyss-main (MirrorEater.java -> ServerBossEvent & SynchedEntityData PHASE)
 * - Bosses-of-Mass-Destruction-FORGE-master (ObsidilithEntity.java -> Purple Notched Boss Bar)
 * - AlexsMobs-1.20 (EntityVoidWorm.java -> Void-safe glowing loot in spawnAtLocation)
 * - Forge-Tutorial-1.20.X-29-attackAnimation (RhinoEntity.java -> AnimationState sync)
 */
public class YmaPytuBossEntity extends Monster {

    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(YmaPytuBossEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_PHASE =
            SynchedEntityData.defineId(YmaPytuBossEntity.class, EntityDataSerializers.INT);

    public static final double MAX_HEALTH = 300.0D;
    public static final double BASE_MOVEMENT_SPEED = 0.25D;
    public static final double BASE_ATTACK_DAMAGE = 12.0D;
    public static final double BASE_ARMOR = 10.0D;
    public static final float ENRAGED_HEALTH_THRESHOLD_RATIO = 0.50F;

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("entity.primordialnight.yma_pytu_boss"),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.NOTCHED_10
    );

    public YmaPytuBossEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 250;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, BASE_MOVEMENT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE)
                .add(Attributes.ARMOR, BASE_ARMOR)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(DATA_PHASE, BossPhase.PHASE_ONE.getPhaseId());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new YmaPytuAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new TeleportBehindTargetGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isAttacking() && this.attackAnimationTimeout <= 0) {
            this.attackAnimationTimeout = 40;
            this.attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.isAttacking()) {
            this.attackAnimationState.stop();
        }
    }

    @Override
    protected void updateWalkAnimation(float partialTick) {
        float walkSpeed = (this.getPose() == Pose.STANDING)
                ? Math.min(partialTick * 6.0F, 1.0F)
                : 0.0F;
        this.walkAnimation.update(walkSpeed, 0.2F);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

        // TODO (Dev 1):
        // 1. Check if current health is below 50% (this.getHealth() <= this.getMaxHealth() * ENRAGED_HEALTH_THRESHOLD_RATIO)
        //    and current phase is BossPhase.PHASE_ONE.
        // 2. If so, call setBossPhase(BossPhase.ENRAGED), which updates bossEvent.setDarkenScreen(true),
        //    increases movement speed by +35% (BossPhase.ENRAGED.getSpeedMultiplier()), and spawns violet flame particles.
        // 3. Maintain the "Horizon of the Night" circular particle barrier around the arena border so neither player nor boss falls into the End void.
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    /**
     * Void-safe Loot Drop (inspired by AlexsMobs EntityVoidWorm.java):
     * Ensures the dropped Voidblade floats and glows so it never falls into the End abyss.
     */
    @Override
    public ItemEntity spawnAtLocation(ItemStack stack) {
        ItemEntity itemEntity = super.spawnAtLocation(stack, 0.0F);
        if (itemEntity != null) {
            itemEntity.setNoGravity(true);
            itemEntity.setGlowingTag(true);
            itemEntity.setExtendedLifetime();
        }
        return itemEntity;
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public BossPhase getBossPhase() {
        return BossPhase.fromId(this.entityData.get(DATA_PHASE));
    }

    public void setBossPhase(BossPhase newPhase) {
        this.entityData.set(DATA_PHASE, newPhase.getPhaseId());
        this.bossEvent.setDarkenScreen(newPhase.shouldDarkenSky());
    }
}
