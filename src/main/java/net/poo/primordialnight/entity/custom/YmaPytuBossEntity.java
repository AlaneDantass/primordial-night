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
    public static final float ENRAGED_HEALTH_THRESHOLD_RATIO = 0.50F; //proporção do limiar de vida para o estado de fúria

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    //
    //
    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("entity.primordialnight.yma_pytu_boss"),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.NOTCHED_10
    );
//=================================================================================================================================================================
    //CONSTRUTOR Do BOSS
    public YmaPytuBossEntity(EntityType<? extends Monster> entityType, Level level) { //padrão: toda entidade precisa de um tipo e nível ao nascr
        super(entityType, level); //chama o construtor da classe Monster
        this.xpReward = 250; //ao morrer, ele dará 250 pontos de experiência (xp).
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, BASE_MOVEMENT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE)
                .add(Attributes.ARMOR, BASE_ARMOR)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
                
                // *! Não adicionei no construtor oficial do bos porque o costrutor vai ser usado para cada monstro que for invocado, mas nosso caso nosso boss terá atributos fixo
                // *! como solução usei a classe auxiliar "ModEventBusEvents" que é a comunicadora dos atributos que o nosso boss terá caso seja invocado no jogo. Agora estamos chamando createAttributes dela*/
            }


    // SINCRONIZAÇÃO DE DADOS ENTRE SERVIDOR E CLIENTE.
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData(); //SynchedData= dados sincronizados com o computador do cliente quando o boss ataca ou muda de fase
        this.entityData.define(ATTACKING, false);
        this.entityData.define(DATA_PHASE, BossPhase.PHASE_ONE.getPhaseId());
    }
    
    //*!=================================KALEBE ESPECIFICA MELHOR==========================================================================
    //essa parte éuma arquitetura nativa para mods do mine

    @Override
    protected void registerGoals() { //quantifica a prioridade do cérebro do boss (goal=meta/objetivo)
        this.goalSelector.addGoal(0, new FloatGoal(this)); //boair na água caso caia
        this.goalSelector.addGoal(1, new YmaPytuAttackGoal(this, 1.0D, true)); //atacar o alvo é prioridade 1 
        this.goalSelector.addGoal(2, new TeleportBehindTargetGoal(this)); // telepostar indo atrás do alvo
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 16.0F)); 
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this)); //olhar ao redor atoa, caso nada esteja acontecendo

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));  //revidar quem bateu nele
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true)); //procurar alvo mais próximo 
    }



    @Override
    public void tick() { //Tick = o "pulso cardíaco"
        super.tick();
        if (this.level().isClientSide()) { //verifica se está do lado do servidor, se sim, está rodano na máquina do jogador renderizando gráficos
            this.setupAnimationStates();
        }

        //*! esse trecho roda 20 por segundo no jogo durante a vida do monstro */
    }

    //RENDERIZAÇÃO DOS GRÁFICOS
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) { //Tempo limite da animação de inatividade

            this.idleAnimationTimeout = this.random.nextInt(40) + 80; //sorteia um novo tempo aleatório entre 80 e 120 ticks e reinicia a animação com .start().
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isAttacking() && this.attackAnimationTimeout <= 0) {
            this.attackAnimationTimeout = 40; // *! permite a diferenciação entre o parâmetro e o objeto
            this.attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout; //enquanto o tempo de inatividade não chega ao limite e ele vai fazendo contagem regressiva
        }

        

        if (!this.isAttacking()) { // se ele não entiver atacando, a animação de ataque para 
            this.attackAnimationState.stop();
        }
    }

    @Override
    protected void updateWalkAnimation(float partialTick) { //updateWalkAnimation = "atualizar animação de caminhada".
        float walkSpeed = (this.getPose() == Pose.STANDING) // STANDING = "em pé"
                ? Math.min(partialTick * 6.0F, 1.0F)
                : 0.0F;
        this.walkAnimation.update(walkSpeed, 0.2F);
    }

    @Override
    protected void customServerAiStep() {
        //! roda a cada tick exclusivamente no servidor. Aqui é onde a barra roxa de vida é recalculada
        //! (vida atual dividida pela vida máxima, dando um valor entre 0.0 e 1.0 para preencher a barra).
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth()); 

        //método executado 20x por seg, então só entra no if se ele tiver na fase 1.
        if( this.getHealth()<=this.getMaxHealth()*ENRAGED_HEALTH_THRESHOLD_RATIO && this.getBossPhase()== BossPhase.PHASE_ONE){ 
            this.setBossPhase(BossPhase.ENRAGED); //Não precisei enviar os parâmetros, pois como o bossPhase é um enum, ele já tem a fase com os parâmetros definidos.
            // this.getAttribute
        // ! ENCAPSULAMENTO: não é possível ter setHealther pois efeitos temporários(muito usados no minecraft) deixariam a lógica do jogo muito bagunçada

        }
        


        // TODO (Dev 1):
        // 1. Check if current health is below 50% (this.getHealth() <= this.getMaxHealth() * ENRAGED_HEALTH_THRESHOLD_RATIO)
        //    and current phase is BossPhase.PHASE_ONE.
        // 2. If so, call setBossPhase(BossPhase.ENRAGED), which updates bossEvent.setDarkenScreen(true),
        //    increases movement speed by +35% (BossPhase.ENRAGED.getSpeedMultiplier()), and spawns violet flame particles.
        // 3. Maintain the "Horizon of the Night" circular particle barrier around the arena border so neither player nor boss falls into the End void.
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer); //startSeenByPlayer = "começou a ser visto pelo jogador".
        this.bossEvent.addPlayer(serverPlayer); //addPlayer / removePlayer = adicionar ou remover jogador da lista de observadores da barra.
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) { //stopSeenByPlayer = "parou de ser visto pelo jogador".
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);

//! o método nativo no mine já faz todo esse trabalho de calcular distância e lógica técnica, mas nossa função é  avisar ao bossEvent pra adicionar visualização ao jogador ou não
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
        this.entityData.set(DATA_PHASE, newPhase.getPhaseId()); //mudou os dados da fase anterior para a próxima que foi enviada como parâmetro e está salva em BossPhase.
        this.bossEvent.setDarkenScreen(newPhase.shouldDarkenSky());
    }
}
