package net.poo.primordialnight.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.poo.primordialnight.entity.custom.YmaPytuBossEntity;

/**
 * 3D Blockbench Model for Yma-Pytu (Titanic Void Golem of 3.8 blocks height,
 * made of shattered obsidian plates with pulsing purple End crystal veins).
 *
 * Responsible: Dev 3 (3D Modeling in Blockbench, Texture Painting & Animations)
 * OOP Concepts:
 * - Inheritance: extends HierarchicalModel<YmaPytuBossEntity>
 * - Polymorphism: @Override setupAnim(), renderToBuffer(), root()
 *
 * Reference Repository: Forge-Tutorial-1.20.X-29-attackAnimation (RhinoModel.java)
 */
public class YmaPytuModel<T extends YmaPytuBossEntity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart body;

    public YmaPytuModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
    }

    /**
     * Placeholder geometry so the mod can be launched and tested immediately (Etapa 2.4).
     * TODO (Dev 3): Replace createBodyLayer() and child ModelParts with the Java code exported from Blockbench.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-12.0F, -60.0F, -8.0F, 24.0F, 60.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        // TODO (Dev 3): Connect walk, idle, and attack animations from ModAnimationDefinitions
        // Example from RhinoModel.java:
        // this.animateWalk(ModAnimationDefinitions.YMA_PYTU_WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        // this.animate(entity.idleAnimationState, ModAnimationDefinitions.YMA_PYTU_IDLE, ageInTicks, 1.0F);
        // this.animate(entity.attackAnimationState, ModAnimationDefinitions.YMA_PYTU_ATTACK, ageInTicks, 1.0F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
