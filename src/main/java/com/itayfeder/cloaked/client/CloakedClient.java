package com.itayfeder.cloaked.client;

import com.itayfeder.cloaked.client.models.CapeItemModel;
import com.itayfeder.cloaked.init.ItemInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

@Environment(EnvType.CLIENT)
public class CloakedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(CapeItemModel.LAYER_LOCATION, CapeItemModel::createBodyLayer);
        BuiltinItemRendererRegistry.INSTANCE.register(ItemInit.CAPE, new CapeISTER());

    }
}
