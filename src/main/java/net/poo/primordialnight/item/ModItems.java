package net.poo.primordialnight.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.entity.ModEntities;
import net.poo.primordialnight.item.custom.EndCompassItem;
import net.poo.primordialnight.item.custom.VoidbladeSwordItem;

/**
 * Centralized registry for all custom items in the mod.
 *
 * Responsible: Dev 4
 * OOP Concepts: Lambdas & Supplier (lazy initialization), Builder Pattern (Item.Properties).
 * Reference: Forge-Tutorial-1.20.X-29-attackAnimation (ModItems.java) & GUIA_BOAS_PRATICAS_ROMHENRI.md
 */
public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PrimordialNightMod.MOD_ID);

    // 1. Boss Spawn Egg (for creative testing and debugging)
    public static final RegistryObject<Item> YMA_PYTU_SPAWN_EGG = ITEMS.register("yma_pytu_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.YMA_PYTU_BOSS, 0x12091A, 0x9B30FF, new Item.Properties()));

    // 2. Danilo's Idea: Void Compass ("Eye of Pytu") pointing to the Sanctuary in The End
    public static final RegistryObject<Item> END_COMPASS = ITEMS.register("end_compass",
            () -> new EndCompassItem(new Item.Properties().stacksTo(1).fireResistant()));

    // 3. Legendary Boss Drop: Voidblade ("The Shard of the First Night")
    public static final RegistryObject<Item> VOIDBLADE_SWORD = ITEMS.register("voidblade_sword",
            () -> new VoidbladeSwordItem(
                    ModToolTiers.PRIMORDIAL_VOID,
                    5,
                    -2.4F,
                    new Item.Properties().durability(2501).fireResistant(),
                    40,
                    1.6D
            ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
