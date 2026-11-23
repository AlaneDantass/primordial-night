package net.poo.primordialnight.datagen.loot;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import net.poo.primordialnight.entity.ModEntities;
import net.poo.primordialnight.item.ModItems;

import java.util.stream.Stream;

public class ModEntityLootTables extends EntityLootSubProvider {

    public ModEntityLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        this.add(ModEntities.YMA_PYTU_BOSS.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.VOIDBLADE_SWORD.get()))));
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return ModEntities.ENTITY_TYPES.getEntries().stream().map(RegistryObject::get);
    }
}