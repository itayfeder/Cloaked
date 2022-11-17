package com.itayfeder.cloaked.mixin;

import com.itayfeder.cloaked.init.ItemInit;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {
    @Shadow @Final private ModelPart cloak;

    public PlayerModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(locals = LocalCapture.CAPTURE_FAILHARD,
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(value = "TAIL"),
            cancellable = true
    )
    private void renderInject(T p_103395_, float p_103396_, float p_103397_, float p_103398_, float p_103399_, float p_103400_, CallbackInfo ci) {
        if (p_103395_.getEquippedStack(EquipmentSlot.CHEST).isEmpty() || p_103395_.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemInit.CAPE)) {
            if (p_103395_.isInSneakingPose()) {
                this.cloak.pivotZ = 2.0F;
                this.cloak.pivotY = 1.85F;
            } else {
                this.cloak.pivotZ = 0.0F;
                this.cloak.pivotY = 0.0F;
            }
        } else if (p_103395_.isInSneakingPose()) {
            this.cloak.pivotZ = 0.3F;
            this.cloak.pivotY = 0.8F;
        } else {
            this.cloak.pivotZ = -1.1F;
            this.cloak.pivotY = -0.85F;
        }
    }
}
