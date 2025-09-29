package dev.lumentae.lattice.status;

import dev.lumentae.lattice.Config;
import dev.lumentae.lattice.util.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class StatusManager {
    public static void setStatus(ServerPlayer player, String status) {
        Config.INSTANCE.status.put(player.getUUID(), status);
    }

    public static Component getStatus(ServerPlayer player) {
        String status = Config.INSTANCE.status.getOrDefault(player.getUUID(), "");
        return TextUtils.parseColoredText(status);
    }

    public static void removeStatus(ServerPlayer player) {
        Config.INSTANCE.status.remove(player.getUUID());
    }
}
