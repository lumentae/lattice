package dev.lumentae.lattice.nickname;

import dev.lumentae.lattice.Config;
import net.minecraft.server.level.ServerPlayer;

public class NicknameManager {
    public static void setNickname(ServerPlayer player, String nickname) {
        Config.INSTANCE.nicknames.put(player.getUUID(), nickname);
    }

    public static String getNickname(ServerPlayer player) {
        String nickName = Config.INSTANCE.nicknames.getOrDefault(player.getUUID(), player.getName().getString());
        if (!nickName.equals(player.getName().getString())) {
            nickName = "~" + nickName;
        }
        return nickName;
    }

    public static void removeNickname(ServerPlayer player) {
        Config.INSTANCE.nicknames.remove(player.getUUID());
    }
}
