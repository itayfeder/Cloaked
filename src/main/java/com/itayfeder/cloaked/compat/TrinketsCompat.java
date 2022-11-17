package com.itayfeder.cloaked.compat;

import com.itayfeder.cloaked.init.ItemInit;
import com.itayfeder.cloaked.items.CapeItem;
import com.itayfeder.cloaked.mixin.CapeLayerMixin;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class TrinketsCompat {
    public static void capeInject(MatrixStack matrixStack, VertexConsumerProvider p_116616_, int p_116617_, AbstractClientPlayerEntity p_116618_, float p_116619_, float h, float p_116621_, float p_116622_, float p_116623_, float p_116624_, CallbackInfo ci, PlayerEntityModel model) {
        TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent(p_116618_).get();
        if (!trinketComponent.getEquipped(ItemInit.CAPE).isEmpty()) {
            Pair<SlotReference, ItemStack> stack = trinketComponent.getEquipped(ItemInit.CAPE).get(0);
            ItemStack itemstack = stack.getRight();
            ItemStack chest = p_116618_.getEquippedStack(EquipmentSlot.CHEST);
            if (itemstack.isOf(ItemInit.CAPE) && p_116618_.isPartVisible(PlayerModelPart.CAPE) && (CapeItem.getId(itemstack) != null) && (chest.getItem() instanceof ArmorItem || chest.isEmpty())) {
                matrixStack.push();
                matrixStack.translate(0.0D, 0.0D, 0.125D);
                double d = MathHelper.lerp((double)h, p_116618_.prevCapeX, p_116618_.capeX) - MathHelper.lerp((double)h, p_116618_.prevX, p_116618_.getX());
                double e = MathHelper.lerp((double)h, p_116618_.prevCapeY, p_116618_.capeY) - MathHelper.lerp((double)h, p_116618_.prevY, p_116618_.getY());
                double m = MathHelper.lerp((double)h, p_116618_.prevCapeZ, p_116618_.capeZ) - MathHelper.lerp((double)h, p_116618_.prevZ, p_116618_.getZ());
                float n = p_116618_.prevBodyYaw + (p_116618_.bodyYaw - p_116618_.prevBodyYaw);
                double o = (double)MathHelper.sin(n * 0.017453292F);
                double p = (double)(-MathHelper.cos(n * 0.017453292F));
                float q = (float)e * 10.0F;
                q = MathHelper.clamp(q, -6.0F, 32.0F);
                float r = (float)(d * o + m * p) * 100.0F;
                r = MathHelper.clamp(r, 0.0F, 150.0F);
                float s = (float)(d * p - m * o) * 100.0F;
                s = MathHelper.clamp(s, -20.0F, 20.0F);
                if (r < 0.0F) {
                    r = 0.0F;
                }

                float t = MathHelper.lerp(h, p_116618_.prevStrideDistance, p_116618_.strideDistance);
                q += MathHelper.sin(MathHelper.lerp(h, p_116618_.prevHorizontalSpeed, p_116618_.horizontalSpeed) * 6.0F) * 32.0F * t;
                if (p_116618_.isInSneakingPose()) {
                    q += 25.0F;
                }

                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(6.0F + r / 2.0F + q));
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(s / 2.0F));
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - s / 2.0F));
                Identifier capeId = CapeItem.getId(itemstack);
                VertexConsumer vertexconsumer = p_116616_.getBuffer(RenderLayer.getEntityCutoutNoCull(new Identifier(capeId.getNamespace(), "textures/cape/" + capeId.getPath() + ".png")));
                model.renderCape(matrixStack, vertexconsumer, p_116617_, OverlayTexture.DEFAULT_UV);
                matrixStack.pop();
                ci.cancel();
            }
        }
    }

    public static void elytraInject(CallbackInfoReturnable <Identifier> cir, LivingEntity entity) {
        TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent(entity).get();
        if (!trinketComponent.getEquipped(ItemInit.CAPE).isEmpty()) {
            Pair<SlotReference, ItemStack> stack = trinketComponent.getEquipped(ItemInit.CAPE).get(0);
            ItemStack itemstack = stack.getRight();
            if (itemstack.isOf(ItemInit.CAPE)) {
                Identifier capeId = CapeItem.getId(itemstack);
                cir.setReturnValue(new Identifier(capeId.getNamespace(), "textures/cape/" + capeId.getPath() + ".png"));
            }
        }
    }
}
