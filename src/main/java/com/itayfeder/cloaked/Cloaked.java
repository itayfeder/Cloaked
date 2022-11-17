package com.itayfeder.cloaked;

import com.itayfeder.cloaked.init.ItemInit;
import com.itayfeder.cloaked.init.LootModifierInit;
import com.itayfeder.cloaked.reload.CapeData;
import com.itayfeder.cloaked.reload.CapeDataReloadListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cloaked implements ModInitializer {
    public static final String MODID = "cloaked";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(CapeDataReloadListener.INSTANCE);
        ItemInit.registerModItems();
        LootModifierInit.modifyLootTables();
    }
}
