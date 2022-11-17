package com.itayfeder.cloaked.mixin;

import com.itayfeder.cloaked.items.CapeItem;
import com.itayfeder.cloaked.reload.CapeDataReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@Mixin(ItemEntry.class)
public class ItemEntryMixin {
    @Shadow @Final private Item item;

    @Inject(locals = LocalCapture.CAPTURE_FAILHARD,
            method = "generateLoot",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void generateLootInject(Consumer<ItemStack> lootConsumer, LootContext context, CallbackInfo ci) {
        if (this.item instanceof CapeItem) {
            ItemStack stack = new ItemStack(this.item);
            List<Identifier> list = CapeDataReloadListener.INSTANCE.capeData.keySet().stream().toList();
            if (!list.isEmpty()) {
                Identifier randomElement = list.get(context.getRandom().nextInt(list.size()));
                CapeItem.setId(stack, randomElement.toString());
            }
            lootConsumer.accept(stack);
            ci.cancel();
        }
    }
}
