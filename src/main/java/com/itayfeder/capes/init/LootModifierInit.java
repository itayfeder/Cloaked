package com.itayfeder.capes.init;

import com.itayfeder.capes.CapesMod;
import com.itayfeder.capes.loot.AddCapeLootModifier;
import com.mojang.serialization.Codec;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootModifierInit {
    public static DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CapesMod.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_CAPE = LOOT_MODIFIERS.register("add_cape", AddCapeLootModifier.CODEC);
}
