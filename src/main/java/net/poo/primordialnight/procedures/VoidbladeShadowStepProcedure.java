package net.poo.primordialnight.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Procedure executed when the player right-clicks with the Voidblade ("Lamina de Pytu").
 * Grants the Boss's "Shadow Step" ability: a short forward dash/teleport that deals AoE damage
 * and spawns shadow particles.
 *
 * Responsible: Dev 4
 * OOP Concepts (GUIA_BOAS_PRATICAS_ROMHENRI.md Section 6.3 & Section 7):
 * - Item delegates heavy action logic to a dedicated Procedure class
 * - Guard clauses for server-side execution
 * - Cooldown and durability management
 */
public class VoidbladeShadowStepProcedure {

    private static final double AOE_DAMAGE_RADIUS = 3.5D;
    private static final float SHADOW_STEP_AOE_DAMAGE = 8.0F;

    public static void execute(Level world, BlockPos originPos, ServerPlayer player,
                               ItemStack bladeStack, int cooldownTicks, double shadowStepForce, int durabilityCost) {
        if (world.isClientSide()) {
            return;
        }

        // TODO (Dev 4):
        // 1. Propel the player forward along player.getLookAngle().scale(shadowStepForce) and set player.hurtMarked = true.
        // 2. Spawn ParticleTypes.PORTAL and ParticleTypes.SQUID_INK via ServerLevel.sendParticles().
        // 3. Deal SHADOW_STEP_AOE_DAMAGE to nearby hostile entities within AOE_DAMAGE_RADIUS.
        // 4. Apply cooldown: player.getCooldowns().addCooldown(bladeStack.getItem(), cooldownTicks).
        // 5. Consume durability: bladeStack.hurtAndBreak(durabilityCost, player, p -> p.broadcastBreakEvent(player.getUsedItemHand())).
    }
}
