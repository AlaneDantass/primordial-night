package net.poo.primordialnight.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.poo.primordialnight.PrimordialNightMod;

/**
 * Registers the dedicated Creative Mode Tab for the mod.
 *
 * Responsible: Dev 4
 * OOP Concepts: Builder Pattern (CreativeModeTab.builder()).
 */
public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PrimordialNightMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> PRIMORDIAL_NIGHT_TAB =
            CREATIVE_MODE_TABS.register("primordial_night_tab",
                    () -> CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.VOIDBLADE_SWORD.get()))
                            .title(Component.translatable("creativetab.primordial_night_tab"))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.YMA_PYTU_SPAWN_EGG.get());
                                output.accept(ModItems.END_COMPASS.get());
                                output.accept(ModItems.VOIDBLADE_SWORD.get());
                            })
                            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
