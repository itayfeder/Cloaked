package com.itayfeder.cloaked.client;

import com.itayfeder.cloaked.Cloaked;
import com.itayfeder.cloaked.client.models.CapeItemModel;
import com.itayfeder.cloaked.init.ItemInit;
import com.itayfeder.cloaked.items.CapeItem;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.http.conn.routing.RouteInfo;

public class CapeISTER implements BuiltinItemRenderer {
    private CapeItemModel CAPE_MODEL;


    @Override
    public void render(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (CAPE_MODEL == null)
            CAPE_MODEL = new CapeItemModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(CapeItemModel.LAYER_LOCATION));

        Item item = stack.getItem();
        if (stack.isOf(ItemInit.CAPE)) {
            matrices.push();

            Identifier capeId = CapeItem.getId(stack);
            VertexConsumer vertexconsumer;
            if (capeId != null) {
                vertexconsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(new Identifier(capeId.getNamespace(), "textures/cape/" + capeId.getPath() + ".png")));
            } else {
                vertexconsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(new Identifier(Cloaked.MODID, "textures/cape/missing.png")));
            }

            this.CAPE_MODEL.render(matrices, vertexconsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

            matrices.pop();

        }
    }
}
