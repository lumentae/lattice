package de.fynn93.servermod.mixin;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Mixin(value = HopperBlockEntity.class, priority = -10000)
public class HopperBlockEntityMixin {
    @Unique
    private static String getItemName(String translationKey) {
        if (translationKey == null) return null;

        String[] names = translationKey.split("\\.");
        return names[names.length - 1];
    }

    @Unique
    private static boolean filterMatch(String filterString, String fullItemName, String itemCustomName) {
        String itemName = getItemName(fullItemName);
        String[] filter = filterString.split(",");
        return Arrays.stream(filter).anyMatch((filter_i) -> {
            if (filter_i.startsWith("$")) {
                // check for tag match
                // format: $tag
                // example: $cobblestone
                return tagMatch(itemName, filter_i.substring(1));
            } else if (filter_i.startsWith("!")) {
                // invert match
                // example: !netherite_sword
                return !FilenameUtils.wildcardMatch(itemName, filter_i.substring(1));
            } else if (filter_i.contains("=")) {
                // check for name match
                // format: type=name
                // example: netherite_sword=Destroyer of worlds
                return nameMatch(itemName, filter_i, itemCustomName);
            }
            return FilenameUtils.wildcardMatch(itemName, filter_i);
        });
    }

    @Unique
    private static boolean tagMatch(String itemName, String filterI) {
        Optional<Reference<Item>> itemOptional = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(itemName));
        if (itemOptional.isEmpty()) return false;

        Holder<Item> item = itemOptional.get();
        List<TagKey<Item>> list = item.tags().toList();

        for (TagKey<Item> tag : list) {
            if (tag.location().getPath().equals(filterI)) {
                return true;
            }
        }

        return false;
    }

    @Unique
    private static boolean nameMatch(String itemName, String filterI, String itemCustomName) {
        Optional<Reference<Item>> itemOptional = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(itemName));
        if (itemOptional.isEmpty()) return false;

        Item item = itemOptional.get().value();

        String[] split = filterI.split("=");
        if (split.length != 2) return false;

        String filterCustomName = split[1];
        return itemCustomName.equals(filterCustomName);
    }

    // pick up items
    @Inject(method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/entity/item/ItemEntity;)Z", at = @At("HEAD"), cancellable = true)
    private static void addItem(Container container, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir) {
        if (!(container instanceof HopperBlockEntity hopperBlockEntity) || hopperBlockEntity.getCustomName() == null) {
            return;
        }
        String itemCustomName = "";
        if (itemEntity.getCustomName() != null) {
            itemCustomName = itemEntity.getCustomName().getString();
        }
        String itemName = getItemName(itemEntity.getItem().getItem().getDescriptionId());
        if (filterMatch(hopperBlockEntity.getCustomName().getString(), itemName, itemCustomName)) {
            return;
        }
        cir.cancel();
    }

    // transfer items
    @Inject(method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void addItem(Container container, Container container2, ItemStack itemStack, Direction direction, CallbackInfoReturnable<ItemStack> cir) {
        if (!(container2 instanceof HopperBlockEntity hopperBlockEntity) || hopperBlockEntity.getCustomName() == null) {
            return;
        }
        String itemCustomName = "";
        if (itemStack.getCustomName() != null) {
            itemCustomName = itemStack.getCustomName().getString();
        }
        if (filterMatch(hopperBlockEntity.getCustomName().getString(), getItemName(itemStack.getItem().getDescriptionId()), itemCustomName)) {
            return;
        }

        cir.setReturnValue(itemStack);
    }
}
