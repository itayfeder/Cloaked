package com.itayfeder.capes.utils;

import com.itayfeder.capes.CapesMod;
import com.itayfeder.capes.client.models.CapeItemModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CapesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onLayerRenderer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CapeItemModel.LAYER_LOCATION, CapeItemModel::createBodyLayer);
    }
}
