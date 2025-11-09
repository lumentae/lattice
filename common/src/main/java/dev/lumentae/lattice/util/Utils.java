package dev.lumentae.lattice.util;

import dev.lumentae.lattice.Config;
import dev.lumentae.lattice.Mod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Utils {
    public static ServerLevel getDimension(ResourceLocation resourceLocation) {
        return Mod.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, resourceLocation));
    }

    public static String getPlayerNameByUUID(UUID playerUUID) {
        AtomicReference<String> returnVal = new AtomicReference<>(playerUUID.toString());
        Objects.requireNonNull(
                        Mod.getServer().services().profileResolver()
                )
                .fetchById(playerUUID)
                .ifPresent(profile ->
                        returnVal.set(profile.name())
                );
        return returnVal.get();
    }

    public static ServerPlayer getPlayerByUUID(UUID playerUUID) {
        return Mod.getServer().getPlayerList().getPlayer(playerUUID);
    }

    public static int getDamage(ItemStack stack) {
        List<ItemAttributeModifiers.Entry> modifiers = stack.getComponents().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY).modifiers();

        for (ItemAttributeModifiers.Entry entry : modifiers) {
            if (entry.attribute().value().getDescriptionId().endsWith("attack_damage"))
                return Double.valueOf(entry.modifier().amount()).intValue();
        }
        return 0;
    }

    public static boolean containsIllegalMods(String mods) {
        List<String> modsList = List.of(mods.toLowerCase().split("\\|"));

        for (String illegalMod : Config.INSTANCE.illegalMods) {
            for (String mod : modsList) {
                if (mod.contains(illegalMod) && !Config.INSTANCE.allowedMods.contains(mod)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ServerPlayer getRandomPlayer() {
        List<ServerPlayer> players = Mod.getServer().getPlayerList().getPlayers();
        if (players.isEmpty()) return null;

        return players.get((int) (Math.random() * players.size()));
    }

    public static <T> T getRandom(List<T> obj) {
        return obj.get((int) (Math.random() * obj.size()));
    }
}
