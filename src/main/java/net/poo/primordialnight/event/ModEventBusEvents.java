package net.poo.primordialnight.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.entity.ModEntities;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;

/**
 * Registers entity attributes (Health, Speed, Attack Damage, Armor, Knockback Resistance)
 * on the Mod Event Bus during game startup.
 *
 * Responsible: Dev 1
 * Reference Repository: Forge-Tutorial-1.20.X-29-attackAnimation (ModEventBusEvents.java)
 */
@Mod.EventBusSubscriber(modid = PrimordialNightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.YMA_PYTU_BOSS.get(), YmaPytuBossEntity.createAttributes().build());
    }
}
