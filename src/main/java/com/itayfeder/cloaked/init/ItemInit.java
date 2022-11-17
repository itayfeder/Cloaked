package com.itayfeder.cloaked.init;

import com.itayfeder.cloaked.Cloaked;
import com.itayfeder.cloaked.items.CapeItem;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemInit {
    public static final Item CAPE = registerItem("cape", new CapeItem((new FabricItemSettings()).equipmentSlot(new EquipmentSlotProvider() {
        @Override
        public EquipmentSlot getPreferredEquipmentSlot(ItemStack stack) {
            return EquipmentSlot.CHEST;
        }
    }).group(ItemGroup.TOOLS).maxCount(1)));

    public static Item registerItem(String name,Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Cloaked.MODID, name), item);
    }

    public static void registerModItems() {

    }
}
