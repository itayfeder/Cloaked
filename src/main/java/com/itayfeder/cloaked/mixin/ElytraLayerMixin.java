package com.itayfeder.cloaked.mixin;

import com.itayfeder.cloaked.compat.curios.CuriosClientCompat;
import com.itayfeder.cloaked.init.ItemInit;
import com.itayfeder.cloaked.items.CapeItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = ElytraLayer.class, remap = false)
public abstract class ElytraLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public ElytraLayerMixin(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }

    @Inject(locals = LocalCapture.CAPTURE_FAILHARD,
            method = "getElytraTexture",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void getElytraTextureInject(ItemStack stack, T entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ItemStack chestStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (chestStack.is(ItemInit.CAPE.get()) && (CapeItem.getId(chestStack) != null)) {
            ResourceLocation capeId = CapeItem.getId(chestStack);
            cir.setReturnValue(new ResourceLocation(capeId.getNamespace(), "textures/cape/" + capeId.getPath() + ".png"));
        }
        if (entity instanceof Player player && ModList.get().isLoaded("curios")) {
            CuriosClientCompat.curiosElytraInject(stack, player, cir);
        }
    }
}