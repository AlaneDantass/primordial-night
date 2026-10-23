package net.poo.primordialnight.entity.ai.attack;

import net.minecraft.world.entity.LivingEntity;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;

/**
 * Strategy Pattern Interface for Yma-Pytu's offensive actions.
 *
 * Responsible: Dev 2
 * OOP Concepts: Interface Contract & Strategy Design Pattern.
 * Reference Repository: Bosses-of-Mass-Destruction-FORGE-master (IAction.java & IActionWithCooldown.java)
 */
public interface BossAttackAction {

    /**
     * Checks whether this attack strategy can be executed in the current tick/phase.
     */
    boolean canUseAttack(YmaPytuBossEntity boss, LivingEntity target);

    /**
     * Executes the offensive action and returns the cooldown in ticks before the next action.
     */
    int performAttack(YmaPytuBossEntity boss, LivingEntity target);
}
