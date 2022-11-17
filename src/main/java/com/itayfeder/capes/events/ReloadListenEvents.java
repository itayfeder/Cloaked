package com.itayfeder.capes.events;

import com.itayfeder.capes.CapesMod;
import com.itayfeder.capes.reload.CapeDataReloadListener;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CapesMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReloadListenEvents {
    @SubscribeEvent
    public static void onServerAboutToStart(AddReloadListenerEvent event) {
        event.addListener(CapeDataReloadListener.INSTANCE);
    }

}
