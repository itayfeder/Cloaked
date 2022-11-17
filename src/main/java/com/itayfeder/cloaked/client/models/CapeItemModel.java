package com.itayfeder.cloaked.client.models;

import com.itayfeder.cloaked.Cloaked;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CapeItemModel extends Model {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(new Identifier(Cloaked.MODID, "cape"), "item");
    private final ModelPart cape;

    public CapeItemModel(ModelPart root) {
        super(RenderLayer::getEntitySolid);
        this.cape = root.getChild("cloak");
    }

    public static TexturedModelData createBodyLayer() {
        ModelData meshdefinition = new ModelData();
        ModelPartData partdefinition = meshdefinition.getRoot();

        ModelPartData cape = partdefinition.addChild("cloak", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -8.0F, -0.5F, 10.0F, 16.0F, 1.0F), ModelTransform.NONE);

        return TexturedModelData.of(meshdefinition, 64, 32);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        cape.render(matrices, vertices, light, overlay, red, green, blue, alpha);

    }
}
