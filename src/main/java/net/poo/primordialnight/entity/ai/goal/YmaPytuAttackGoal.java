package net.poo.primordialnight.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.poo.primordialnight.entity.ai.attack.BossAttackAction;
import net.poo.primordialnight.entity.ai.attack.GravityOrbAttack;
import net.poo.primordialnight.entity.ai.attack.VoidSlamAttack;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;

import java.util.List;

/**
 * Main Combat AI Goal for Yma-Pytu.
 * Coordinates melee animation timing and dynamically selects special attacks using the Strategy Pattern.
 *
 * Responsible: Dev 2
 * OOP Concepts:
 * - Inheritance: extends MeleeAttackGoal
 * - Polymorphism & Strategy Pattern: iterates over List<BossAttackAction> strategies
 *
 * Reference Repositories:
 * - Forge-Tutorial-1.20.X-29-attackAnimation (RhinoAttackGoal.java)
 * - Bosses-of-Mass-Destruction-FORGE-master (ObsidilithMoveLogic.java)
 */
public class YmaPytuAttackGoal extends MeleeAttackGoal {

    private final YmaPytuBossEntity bossEntity;
    private final List<BossAttackAction> attackStrategies;
    private int specialAttackCooldownTicks = 40;

    public YmaPytuAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        this.bossEntity = (YmaPytuBossEntity) mob;
        this.attackStrategies = List.of(
                new VoidSlamAttack(),
                new GravityOrbAttack()
        );
    }

    @Override
    public void tick() {
        super.tick();
        if (this.specialAttackCooldownTicks > 0) {
            --this.specialAttackCooldownTicks;
        }

        LivingEntity target = this.bossEntity.getTarget();
        if (target != null && this.specialAttackCooldownTicks <= 0) {
            // TODO (Dev 2): Select an eligible BossAttackAction from attackStrategies where canUseAttack() is true,
            // call performAttack(bossEntity, target), and set specialAttackCooldownTicks to the returned cooldown.
        }
    }

    @Override
    public void stop() {
        this.bossEntity.setAttacking(false);
        super.stop();
    }
}
