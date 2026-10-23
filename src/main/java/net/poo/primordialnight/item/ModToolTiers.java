package net.poo.primordialnight.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.util.ModTags;

import java.util.List;

/**
 * Custom weapon/tool material tier above Netherite for the Voidblade.
 *
 * OOP Concepts: Interface Implementation (Tier), Composition.
 * Reference: GUIA_BOAS_PRATICAS_ROMHENRI.md (Section 5.3 & Section 7 #9)
 */
public class ModToolTiers {

    public static final Tier PRIMORDIAL_VOID = TierSortingRegistry.registerTier(
            new ForgeTier(5, 2501, 10.0F, 5.0F, 22,
                    ModTags.Blocks.NEEDS_PRIMORDIAL_VOID_TOOL, () -> Ingredient.EMPTY),
            new ResourceLocation(PrimordialNightMod.MOD_ID, "primordial_void"),
            List.of(Tiers.NETHERITE),
            List.of()
    );
}
