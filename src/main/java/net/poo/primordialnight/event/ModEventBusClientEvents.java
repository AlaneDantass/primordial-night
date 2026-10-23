package net.poo.primordialnight.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.entity.client.ModModelLayers;
import net.poo.primordialnight.entity.client.YmaPytuModel;

/**
 * Registers the 3D Blockbench LayerDefinition on the Client Mod Event Bus.
 *
 * Responsible: Dev 3
 * Reference Repository: Forge-Tutorial-1.20.X-29-attackAnimation (ModEventBusClientEvents.java)
 */
@Mod.EventBusSubscriber(modid = PrimordialNightMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.YMA_PYTU_LAYER, YmaPytuModel::createBodyLayer);
    }
}
