package net.poo.primordialnight.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.poo.primordialnight.PrimordialNightMod;

/**
 * Data Generation orchestrator (generates JSON recipes, item models, tags, and loot tables via Java code).
 *
 * Responsible: Dev 4
 * OOP Concepts: Inheritance in Forge Data Providers (GUIA_BOAS_PRATICAS_ROMHENRI.md Section 6.5).
 * Reference: Forge-Tutorial-1.20.X-29-attackAnimation (DataGenerators.java)
 */
@Mod.EventBusSubscriber(modid = PrimordialNightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent


    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(packOutput));
    }
}