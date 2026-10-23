package net.poo.primordialnight.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.poo.primordialnight.PrimordialNightMod;

/**
 * Custom Block and Item Tags organized using Static Nested Classes.
 *
 * OOP Concepts: Static Nested Classes (GUIA_BOAS_PRATICAS_ROMHENRI.md Section 5.5).
 */
public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> NEEDS_PRIMORDIAL_VOID_TOOL = tag("needs_primordial_void_tool");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(PrimordialNightMod.MOD_ID, name));
        }
    }

    public static class Items {
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(PrimordialNightMod.MOD_ID, name));
        }
    }
}
