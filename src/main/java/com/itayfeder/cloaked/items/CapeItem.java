package com.itayfeder.cloaked.items;

import com.itayfeder.cloaked.reload.CapeData;
import com.itayfeder.cloaked.reload.CapeDataReloadListener;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.stat.Stats;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.gen.BlockSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

public class CapeItem extends Item implements Wearable {
    private static final String TAG_ID = "Id";

    public static final DispenserBehavior DISPENSE_ITEM_BEHAVIOR = new ItemDispenserBehavior() {
        protected ItemStack dispenseSilently(BlockPointer p_40408_, ItemStack p_40409_) {
            return CapeItem.dispenseArmor(p_40408_, p_40409_) ? p_40409_ : super.dispenseSilently(p_40408_, p_40409_);
        }
    };
    public static boolean dispenseArmor(BlockPointer p_40399_, ItemStack p_40400_) {
        BlockPos blockpos = p_40399_.getPos().offset(p_40399_.getBlockState().get(DispenserBlock.FACING));
        List<PlayerEntity> list = p_40399_.getWorld().getEntitiesByClass(PlayerEntity.class, new Box(blockpos), EntityPredicates.EXCEPT_SPECTATOR.and(new MobCanWearCapeEntitySelector(p_40400_)));
        if (list.isEmpty()) {
            return false;
        } else {
            PlayerEntity livingentity = list.get(0);
            EquipmentSlot equipmentslot = EquipmentSlot.CHEST;
            ItemStack itemstack = p_40400_.split(1);
            livingentity.equipStack(equipmentslot, itemstack);

            return true;
        }
    }

    public CapeItem(Settings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            for(Map.Entry<Identifier, CapeData> cape : CapeDataReloadListener.INSTANCE.capeData.entrySet()) {
                ItemStack stack = new ItemStack(this);
                setId(stack, cape.getKey().toString());
                stacks.add(stack);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemstack = user.getStackInHand(hand);
        EquipmentSlot equipmentslot = EquipmentSlot.CHEST;
        ItemStack itemstack1 = user.getEquippedStack(equipmentslot);
        if (itemstack1.isEmpty()) {
            user.equipStack(equipmentslot, itemstack.copy());
            if (!world.isClient) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
            }

            itemstack.setCount(0);
            return world.isClient() ? TypedActionResult.success(itemstack) : TypedActionResult.consume(itemstack);
        } else {
            return TypedActionResult.fail(itemstack);
        }
    }

    public static Identifier getId(ItemStack p_40933_) {
        NbtCompound compoundtag = p_40933_.getNbt();
        if (compoundtag != null) {
            String[] parts = compoundtag.getString(TAG_ID).split(":");
            if (parts.length >= 2) {
                return new Identifier(parts[0], parts[1]);
            }
        }
        return null;
    }

    public static void setId(ItemStack p_40885_, String p_40886_) {
        NbtCompound compoundtag = p_40885_.getOrCreateNbt();
        compoundtag.putString(TAG_ID, p_40886_);
    }

    public static NbtCompound getRandom() {
        NbtCompound tag = new NbtCompound();
        Random rand = new Random();
        List<Identifier> list = CapeDataReloadListener.INSTANCE.capeData.keySet().stream().toList();
        if (!list.isEmpty()) {
            Identifier randomElement = list.get(rand.nextInt(list.size()));

            tag.putString(TAG_ID, randomElement.toString());
        }
        return tag;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        Identifier id = getId(stack);

        if (id != null && CapeDataReloadListener.INSTANCE.capeData.containsKey(id)) {
            tooltip.add(Text.translatable(CapeDataReloadListener.INSTANCE.capeData.get(id).translationName).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }
    }

    public static class MobCanWearCapeEntitySelector implements Predicate<Entity> {
        private final ItemStack itemStack;

        public MobCanWearCapeEntitySelector(ItemStack p_20445_) {
            this.itemStack = p_20445_;
        }

        public boolean test(@Nullable Entity p_20447_) {
            if (!p_20447_.isAlive()) {
                return false;
            } else if (!(p_20447_ instanceof PlayerEntity)) {
                return false;
            } else {
                PlayerEntity livingentity = (PlayerEntity)p_20447_;
                return livingentity.canEquip(this.itemStack);
            }
        }
    }
}
