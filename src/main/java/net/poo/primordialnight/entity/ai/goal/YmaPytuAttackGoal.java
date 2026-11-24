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

        // Decrementa o cooldown global de ataques especiais a cada tick
        if (this.specialAttackCooldownTicks > 0) {
            --this.specialAttackCooldownTicks;
        }

        LivingEntity target = this.bossEntity.getTarget();

        // Só tenta usar um ataque especial se há alvo e o cooldown já zerou
        if (target != null && this.specialAttackCooldownTicks <= 0) {

            // ── Strategy Pattern: iterar sobre as estratégias de ataque ───────────
            // Percorre a lista em ordem (VoidSlamAttack tem prioridade sobre GravityOrbAttack).
            // O primeiro ataque elegível (canUseAttack() == true) é executado.
            for (BossAttackAction strategy : this.attackStrategies) {
                if (strategy.canUseAttack(this.bossEntity, target)) {

                    // Polimorfismo: performAttack() chama o executeAttack() correto
                    // de cada subclasse (VoidSlamAttack ou GravityOrbAttack).
                    // Retorna o cooldown em ticks daquele ataque específico.
                    this.specialAttackCooldownTicks = strategy.performAttack(this.bossEntity, target);

                    // Ativa a animação de ataque visível no cliente
                    this.bossEntity.setAttacking(true);

                    // Para na primeira estratégia elegível — apenas um ataque por ciclo
                    break;
                }
            }
        }
    }

    @Override
    public void stop() {
        this.bossEntity.setAttacking(false);
        super.stop();
    }
}
