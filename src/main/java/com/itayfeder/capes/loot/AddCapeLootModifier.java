package com.itayfeder.capes.loot;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.itayfeder.capes.init.ItemInit;
import com.itayfeder.capes.items.CapeItem;
import com.itayfeder.capes.reload.CapeData;
import com.itayfeder.capes.reload.CapeDataReloadListener;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddCapeLootModifier extends LootModifier {

    public static final Supplier<Codec<AddCapeLootModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, AddCapeLootModifier::new)));

    protected AddCapeLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if(context.getRandom().nextFloat() >= 0.95f && context.getQueriedLootTableId().toString().contains("chest")) {
            Set<Map.Entry<ResourceLocation, CapeData>> values = CapeDataReloadListener.INSTANCE.capeData.entrySet();
            List<ResourceLocation> valueArr = new ArrayList<>();

            for (Map.Entry<ResourceLocation, CapeData> x : values)
                if (x.getValue().treasure)
                    valueArr.add(x.getKey());

            ItemStack stack = new ItemStack(ItemInit.CAPE.get());
            CapeItem.setId(stack, valueArr.get(Mth.nextInt(context.getRandom(), 0, values.size())).toString());
            generatedLoot.add(stack);
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
