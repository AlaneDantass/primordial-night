package net.poo.primordialnight.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.poo.primordialnight.PrimordialNightMod;

/**
 * Defines the ModelLayerLocation IDs used to link the 3D Blockbench geometry to the Renderer.
 *
 * Responsible: Dev 3
 * Reference Repository: Forge-Tutorial-1.20.X-29-attackAnimation (ModModelLayers.java)
 */
public class ModModelLayers {

    public static final ModelLayerLocation YMA_PYTU_LAYER = new ModelLayerLocation(
            new ResourceLocation(PrimordialNightMod.MOD_ID, "yma_pytu_layer"),
            "main"
    );
}
