package com.itayfeder.cloaked.events;

import com.itayfeder.cloaked.CapesMod;
import com.itayfeder.cloaked.reload.CapeDataReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CapesMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReloadListenEvents {
    @SubscribeEvent
    public static void onServerAboutToStart(AddReloadListenerEvent event) {
        event.addListener(CapeDataReloadListener.INSTANCE);
    }

}
