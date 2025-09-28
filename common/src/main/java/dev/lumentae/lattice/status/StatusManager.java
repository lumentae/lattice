package dev.lumentae.lattice.status;

import dev.lumentae.lattice.Config;
import net.minecraft.server.level.ServerPlayer;

public class StatusManager {
    public static void setStatus(ServerPlayer player, String status) {
        Config.INSTANCE.status.put(player.getUUID(), status);
    }

    public static net.minecraft.network.chat.Component getStatus(ServerPlayer player) {
        String status = Config.INSTANCE.status.getOrDefault(player.getUUID(), "");
        return net.minecraft.network.chat.Component.literal(status);
    }

    public static void removeStatus(ServerPlayer player) {
        Config.INSTANCE.status.remove(player.getUUID());
    }
}
