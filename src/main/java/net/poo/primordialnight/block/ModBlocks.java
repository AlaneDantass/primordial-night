package net.poo.primordialnight.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.item.ModItems;

import java.util.function.Supplier;

/**
 * Centralized registry for custom blocks using DeferredRegister and Java Generics.
 *
 * OOP Concepts: Generics (<T extends Block>), Functional Interfaces (Supplier<T>).
 * Reference: GUIA_BOAS_PRATICAS_ROMHENRI.md (Section 5.6 & 6.1)
 */
public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PrimordialNightMod.MOD_ID);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
