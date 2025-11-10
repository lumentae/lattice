package dev.lumentae.lattice.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class PacketUtils {
    public static void sendToServer(CustomPacketPayload payload) {
        ClientPacketListener listener = Objects.requireNonNull(Minecraft.getInstance().getConnection());
        listener.send(new ServerboundCustomPayloadPacket(payload));
    }

    public static void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
        player.connection.send(new ClientboundCustomPayloadPacket(payload));
    }
}
