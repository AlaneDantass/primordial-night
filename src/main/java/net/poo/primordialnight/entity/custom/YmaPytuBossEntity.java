package net.poo.primordialnight.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
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

        if (this.level() instanceof ServerLevel serverLevel) {//mantem a barreira ativa durante toda batalha
            this.spawnArenaBoundaryParticles(serverLevel);
        }

        //!Nesse método ele verifica a transição para o modo fúria
        //método executado 20x por seg, então só entra no if se ele tiver na fase 1.
        if( this.getHealth()<=this.getMaxHealth()*ENRAGED_HEALTH_THRESHOLD_RATIO && this.getBossPhase()== BossPhase.PHASE_ONE){ 
            this.setBossPhase(BossPhase.ENRAGED); //Não precisei enviar os parâmetros, pois como o bossPhase é um enum, ele já tem a fase com os parâmetros definidos.
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BASE_MOVEMENT_SPEED*BossPhase.ENRAGED.getSpeedMultiplier());
        // ! ENCAPSULAMENTO: não é possível ter setHealther pois efeitos temporários(muito usados no minecraft) deixariam a lógica do jogo muito bagunçada
        //this.getAttribute(Attributes.MOVEMENT_SPEED) retorna uma instância padrão que genrencia a velocidade desse monstro específico
        //setBaseValue: por padrão o minecraft já tem uma velocidade padrão dele, precisamos chamar o set pra isso
        //BossPhase.ENRAGED.getSpeedMultiplier() :pega o multiplicador 


        //BossPhase: É uma tabela de consulta.
        //Attributes.MOVEMENT_SPEED: etiqueta que especidifa o que exatamente ele tá falando no jogo, que é a velocidade de andar
        //this.getAttribute: É o objeto real na memória daquele monstro específico que
            //! Efeito de Explosão no modo fúria
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                ParticleTypes.PORTAL, // 1. O tipo da partícula (partículas roxas do portal)
                this.getX(),          // 2. Coordenada X (onde o Boss está)
                this.getY() + 1.5,    // 3. Coordenada Y (na altura do peito do Boss)
                this.getZ(),          // 4. Coordenada Z (onde o Boss está)
                50,                   // 5. Quantidade de partículas (uma explosão de 50)
                0.5, 0.5, 0.5,        // 6. Espalhamento (meio bloco para cada lado)
                0.1                   // 7. Velocidade em que elas voam
            );
            }
        
        }
    }

    //Aqui estamos definindo o círculo da arena de Luta ao redor do Boss
    private void spawnArenaBoundaryParticles(ServerLevel serverLevel){
        //o boss só solt apartículas a cada 10 ticks pra economizar desempenho
        if(this.tickCount%10!=0){ //tickCount é o contador de vida padrão do jogo
            return;
        }

        final double  ARENA_BARRIER_RADIUS = 16.0;
        final int PARTICLE_COUNT = 24; // Quantos pontos ao redor do círculo

        for(int i = 0; i < PARTICLE_COUNT; i++){
            double angle = (2 * Math.PI / PARTICLE_COUNT) * i;
            double offsetX = Math.cos(angle) * ARENA_BARRIER_RADIUS;
            double offsetZ = Math.sin(angle) * ARENA_BARRIER_RADIUS;

            serverLevel.sendParticles(
                ParticleTypes.WITCH, // partícula de magia roxa (padrão)
                this.getX() + offsetX,
                this.getY() + 0.5,
            this.getZ() + offsetZ,
            1, 0.0, 0.0, 0.0, 0.0
            );
        }
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


//! CHECK LIST:
/*Barreira da Arena: Você moveu a chamada this.spawnArenaBoundaryParticles(serverLevel) para fora do if, garantindo que ela rode durante toda a luta!
-Método Auxiliar spawnArenaBoundaryParticles: Cálculo trigonométrico (2𝜋/24) com Math.cos e Math.sin, raio de 16 blocos e proteção de performance com tickCount % 10 != 0.
-Máquina de Estados e Fúria: Condição estrita checando proporção de vida e BossPhase.PHASE_ONE.
-Ajuste Dinâmico de Velocidade: Atualização no Attributes.MOVEMENT_SPEED usando o multiplicador encapsulado do Enum.
-Efeito Visual da Fúria: Disparo das partículas de portal na altura do peito do Boss (𝑌+1.5).
*/

//! Vamos usar, entãoo Graceful Degradation e tolerância a Falhas
/*
Vamos criar uma excessão personalizada que herda de Exception, se usássemos o RuntimeException teríamos que tratar a falha imediatamente, gerando um crash no jogo
por isso vamos criar um bloco try catch se o fromId lançar uma exceção, segura o erro no ar antes que ele derrube o jogo, registra a falha, continua no PHASE_ONE para a luta continuar
*/

