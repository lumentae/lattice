package dev.lumentae.lattice;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Objects;

public class PacketDistributor {
    public static void sendToServer(CustomPacketPayload payload) {
        ClientPacketListener listener = Objects.requireNonNull(Minecraft.getInstance().getConnection());
        listener.send(new ServerboundCustomPayloadPacket(payload));
    }
}
