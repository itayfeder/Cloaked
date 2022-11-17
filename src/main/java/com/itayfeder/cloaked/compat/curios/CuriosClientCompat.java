package com.itayfeder.cloaked.compat.curios;

import com.itayfeder.cloaked.init.ItemInit;
import com.itayfeder.cloaked.items.CapeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class CuriosClientCompat {
    public static void curiosCapeInject(PoseStack p_116615_, MultiBufferSource p_116616_, int p_116617_, AbstractClientPlayer p_116618_, float p_116619_, float p_116620_, float p_116621_, float p_116622_, float p_116623_, float p_116624_, CallbackInfo ci, PlayerModel model) {
        ItemStack itemstack = getItemAtBack(p_116618_);
        ItemStack chest = p_116618_.getItemBySlot(EquipmentSlot.CHEST);
        if (itemstack != null && (chest.getItem() instanceof ArmorItem || chest.isEmpty())) {
            if (itemstack.is(ItemInit.CAPE.get()) && p_116618_.isModelPartShown(PlayerModelPart.CAPE) && (CapeItem.getId(itemstack) != null)) {
                p_116615_.pushPose();
                p_116615_.translate(0.0D, 0.0D, 0.125D);
                double d0 = Mth.lerp((double)p_116621_, p_116618_.xCloakO, p_116618_.xCloak) - Mth.lerp((double)p_116621_, p_116618_.xo, p_116618_.getX());
                double d1 = Mth.lerp((double)p_116621_, p_116618_.yCloakO, p_116618_.yCloak) - Mth.lerp((double)p_116621_, p_116618_.yo, p_116618_.getY());
                double d2 = Mth.lerp((double)p_116621_, p_116618_.zCloakO, p_116618_.zCloak) - Mth.lerp((double)p_116621_, p_116618_.zo, p_116618_.getZ());
                float f = p_116618_.yBodyRotO + (p_116618_.yBodyRot - p_116618_.yBodyRotO);
                double d3 = (double)Mth.sin(f * ((float)Math.PI / 180F));
                double d4 = (double)(-Mth.cos(f * ((float)Math.PI / 180F)));
                float f1 = (float)d1 * 10.0F;
                f1 = Mth.clamp(f1, -6.0F, 32.0F);
                float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
                f2 = Mth.clamp(f2, 0.0F, 150.0F);
                float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
                f3 = Mth.clamp(f3, -20.0F, 20.0F);
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }

                float f4 = Mth.lerp(p_116621_, p_116618_.oBob, p_116618_.bob);
                f1 += Mth.sin(Mth.lerp(p_116621_, p_116618_.walkDistO, p_116618_.walkDist) * 6.0F) * 32.0F * f4;
                if (p_116618_.isCrouching()) {
                    f1 += 25.0F;
                }

                p_116615_.mulPose(Vector3f.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
                p_116615_.mulPose(Vector3f.ZP.rotationDegrees(f3 / 2.0F));
                p_116615_.mulPose(Vector3f.YP.rotationDegrees(180.0F - f3 / 2.0F));
                ResourceLocation capeId = CapeItem.getId(itemstack);
                VertexConsumer vertexconsumer = p_116616_.getBuffer(RenderType.entityCutoutNoCull(new ResourceLocation(capeId.getNamespace(), "textures/cape/" + capeId.getPath() + ".png")));
                model.renderCloak(p_116615_, vertexconsumer, p_116617_, OverlayTexture.NO_OVERLAY);
                p_116615_.popPose();
                ci.cancel();
            }
        }

    }

    public static void curiosElytraInject(ItemStack stack, Player entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ItemStack chestStack = getItemAtBack(entity);
        if (chestStack != null) {
            if (chestStack.is(ItemInit.CAPE.get()) && (CapeItem.getId(chestStack) != null)) {
                ResourceLocation capeId = CapeItem.getId(chestStack);
                cir.setReturnValue(new ResourceLocation(capeId.getNamespace(), "textures/cape/" + capeId.getPath() + ".png"));
            }
        }

    }


    public static ItemStack getItemAtBack(Player player) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> triplet = CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.CAPE.get(), player);
        if (!triplet.isEmpty()) {
            return triplet.get().getRight();
        }
        return null;
    }
}
