package com.itayfeder.capes.init;

import com.itayfeder.capes.CapesMod;
import com.itayfeder.capes.items.CapeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CapesMod.MODID);

    public static final RegistryObject<Item> CAPE = ITEMS.register("cape", () -> new CapeItem((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_TOOLS)));
}
