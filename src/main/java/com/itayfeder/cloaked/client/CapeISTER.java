package com.itayfeder.cloaked.client;

import com.itayfeder.cloaked.CapesMod;
import com.itayfeder.cloaked.client.models.CapeItemModel;
import com.itayfeder.cloaked.init.ItemInit;
import com.itayfeder.cloaked.items.CapeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CapeISTER extends BlockEntityWithoutLevelRenderer {
    private CapeItemModel CAPE_MODEL = new CapeItemModel(Minecraft.getInstance().getEntityModels().bakeLayer(CapeItemModel.LAYER_LOCATION));

    public CapeISTER() {
        super(null, null);
    }

    @Override
    public void onResourceManagerReload(ResourceManager p_172555_) {
        this.CAPE_MODEL = new CapeItemModel(Minecraft.getInstance().getEntityModels().bakeLayer(CapeItemModel.LAYER_LOCATION));
    }

    @Override
    public void renderByItem(ItemStack p_108830_, ItemTransforms.TransformType p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_) {
        Item item = p_108830_.getItem();
        if (p_108830_.is(ItemInit.CAPE.get())) {
            p_108832_.pushPose();

            ResourceLocation capeId = CapeItem.getId(p_108830_);
            VertexConsumer vertexconsumer;
            if (capeId != null) {
                vertexconsumer = p_108833_.getBuffer(RenderType.entityCutoutNoCull(new ResourceLocation(capeId.getNamespace(), "textures/cape/" + capeId.getPath() + ".png")));
            } else {
                vertexconsumer = p_108833_.getBuffer(RenderType.entityCutoutNoCull(new ResourceLocation(CapesMod.MODID, "textures/cape/missing.png")));
            }

            this.CAPE_MODEL.renderToBuffer(p_108832_, vertexconsumer, p_108834_, p_108835_, 1.0F, 1.0F, 1.0F, 1.0F);

            p_108832_.popPose();

        }
    }
}
