package net.poo.primordialnight.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.poo.primordialnight.procedures.VoidbladeShadowStepProcedure;

/**
 * The Legendary Boss Drop: Voidblade ("The Shard of the First Night" / "Lamina de Pytu").
 * Crystallized from the core of Yma-Pytu upon defeat.
 *
 * Responsible: Dev 4
 * OOP Concepts Applied (GUIA_BOAS_PRATICAS_ROMHENRI.md Section 5.1, 5.2 & 6.3):
 * - Inheritance: extends SwordItem and calls super(...) in the constructor
 * - Encapsulation: private final attributes (cooldownTicks, shadowStepForce) configured via constructor
 * - Polymorphism (@Override):
 *     1. hurtEnemy(): applies Darkness and Slowness debuffs on hit, then calls super.hurtEnemy()
 *     2. use(): triggers the Boss's Shadow Step dash + AoE damage via VoidbladeShadowStepProcedure
 *
 * Reference Repositories:
 * - GUIA_BOAS_PRATICAS_ROMHENRI.md (BurneriteSword & BoostSwordItem)
 * - Astram_Anvishya_Asuram-main (VajraItem.java)
 */
public class VoidbladeSwordItem extends SwordItem {

    private static final int DARKNESS_DURATION_TICKS = 60;
    private static final int SLOWNESS_DURATION_TICKS = 60;
    private static final int DURABILITY_COST_ON_DASH = 1;

    private final int cooldownTicks;
    private final double shadowStepForce;

    public VoidbladeSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier,
                              Properties properties, int cooldownTicks, double shadowStepForce) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        this.cooldownTicks = cooldownTicks;
        this.shadowStepForce = shadowStepForce;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide()) {
            if (!target.level().isClientSide()){
                target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, DARKNESS_DURATION_TICKS));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWNESS_DURATION_TICKS));
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack bladeStack = player.getItemInHand(usedHand);
        if (level.isClientSide()) {
            return InteractionResultHolder.pass(bladeStack);
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.pass(bladeStack);
        }

        VoidbladeShadowStepProcedure.execute(
                level,
                serverPlayer.blockPosition(),
                serverPlayer,
                bladeStack,
                this.cooldownTicks,
                this.shadowStepForce,
                DURABILITY_COST_ON_DASH
        );

        return InteractionResultHolder.success(bladeStack);
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }

    public double getShadowStepForce() {
        return this.shadowStepForce;
    }
}
