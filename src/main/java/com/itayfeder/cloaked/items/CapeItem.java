package com.itayfeder.cloaked.items;

import com.itayfeder.cloaked.client.CapeISTER;
import com.itayfeder.cloaked.reload.CapeData;
import com.itayfeder.cloaked.reload.CapeDataReloadListener;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.NonNullList;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CapeItem extends Item implements Wearable {
    private static final String TAG_ID = "Id";

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected ItemStack execute(BlockSource p_40408_, ItemStack p_40409_) {
            return CapeItem.dispenseArmor(p_40408_, p_40409_) ? p_40409_ : super.execute(p_40408_, p_40409_);
        }
    };
    public static boolean dispenseArmor(BlockSource p_40399_, ItemStack p_40400_) {
        BlockPos blockpos = p_40399_.getPos().relative(p_40399_.getBlockState().getValue(DispenserBlock.FACING));
        List<Player> list = p_40399_.getLevel().getEntitiesOfClass(Player.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS.and(new MobCanWearCapeEntitySelector(p_40400_)));
        if (list.isEmpty()) {
            return false;
        } else {
            Player livingentity = list.get(0);
            EquipmentSlot equipmentslot = EquipmentSlot.CHEST;
            ItemStack itemstack = p_40400_.split(1);
            livingentity.setItemSlot(equipmentslot, itemstack);

            return true;
        }
    }


    public CapeItem(Properties p_41383_) {
        super(p_41383_);
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            for(Map.Entry<ResourceLocation, CapeData> cape : CapeDataReloadListener.INSTANCE.capeData.entrySet()) {
                ItemStack stack = new ItemStack(this);
                setId(stack, cape.getKey().toString());
                p_41392_.add(stack);
            }
        }
    }

    public InteractionResultHolder<ItemStack> use(Level p_41137_, Player p_41138_, InteractionHand p_41139_) {
        ItemStack itemstack = p_41138_.getItemInHand(p_41139_);
        EquipmentSlot equipmentslot = EquipmentSlot.CHEST;
        ItemStack itemstack1 = p_41138_.getItemBySlot(equipmentslot);
        if (itemstack1.isEmpty()) {
            p_41138_.setItemSlot(equipmentslot, itemstack.copy());
            if (!p_41137_.isClientSide()) {
                p_41138_.awardStat(Stats.ITEM_USED.get(this));
            }

            itemstack.setCount(0);
            return InteractionResultHolder.sidedSuccess(itemstack, p_41137_.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }



    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return armorType == EquipmentSlot.CHEST;
    }

    public static ResourceLocation getId(ItemStack p_40933_) {
        CompoundTag compoundtag = p_40933_.getTag();
        if (compoundtag != null) {
            String[] parts = compoundtag.getString(TAG_ID).split(":");
            return new ResourceLocation(parts[0], parts[1]);
        }
        return null;
    }

    public static void setId(ItemStack p_40885_, String p_40886_) {
        CompoundTag compoundtag = p_40885_.getOrCreateTag();
        compoundtag.putString(TAG_ID, p_40886_);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new CapeISTER();
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        ResourceLocation id = getId(p_41421_);

        if (id != null && CapeDataReloadListener.INSTANCE.capeData.containsKey(id)) {
            p_41423_.add(Component.translatable(CapeDataReloadListener.INSTANCE.capeData.get(id).translationName).withStyle(ChatFormatting.GRAY));
        }
    }

    public static class MobCanWearCapeEntitySelector implements Predicate<Entity> {
        private final ItemStack itemStack;

        public MobCanWearCapeEntitySelector(ItemStack p_20445_) {
            this.itemStack = p_20445_;
        }

        public boolean test(@javax.annotation.Nullable Entity p_20447_) {
            if (!p_20447_.isAlive()) {
                return false;
            } else if (!(p_20447_ instanceof Player)) {
                return false;
            } else {
                Player livingentity = (Player)p_20447_;
                return livingentity.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
            }
        }
    }
}
