package net.poo.primordialnight.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.poo.primordialnight.PrimordialNightMod;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;
import net.poo.primordialnight.entity.enums.BossPhase;

/**
 * Client Renderer for Yma-Pytu.
 * Switches textures dynamically when the Boss transitions from PHASE_ONE to ENRAGED
 * (opening its chest core in violet flames and changing eye color).
 *
 * Responsible: Dev 3
 * OOP Concepts:
 * - Inheritance: extends MobRenderer<YmaPytuBossEntity, YmaPytuModel<YmaPytuBossEntity>>
 * - Polymorphism: @Override getTextureLocation() & render()
 *
 * Reference Repository: Forge-Tutorial-1.20.X-29-attackAnimation (RhinoRenderer.java)
 */
public class YmaPytuRenderer extends MobRenderer<YmaPytuBossEntity, YmaPytuModel<YmaPytuBossEntity>> {

    private static final ResourceLocation PHASE_ONE_TEXTURE =
            new ResourceLocation(PrimordialNightMod.MOD_ID, "textures/entity/yma_pytu_phase_one.png");
    private static final ResourceLocation ENRAGED_TEXTURE =
            new ResourceLocation(PrimordialNightMod.MOD_ID, "textures/entity/yma_pytu_enraged.png");

    public YmaPytuRenderer(EntityRendererProvider.Context context) {
        super(context, new YmaPytuModel<>(context.bakeLayer(ModModelLayers.YMA_PYTU_LAYER)), 1.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(YmaPytuBossEntity entity) {
        if (entity.getBossPhase() == BossPhase.ENRAGED) {
            return ENRAGED_TEXTURE;
        }
        return PHASE_ONE_TEXTURE;
    }

    @Override
    public void render(YmaPytuBossEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
