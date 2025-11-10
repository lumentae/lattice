package dev.lumentae.lattice.packet;

import dev.lumentae.lattice.Config;
import dev.lumentae.lattice.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ClientboundRulesPacket(String rules) implements CustomPacketPayload {
    public static final Type<ClientboundRulesPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "rules"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundRulesPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ClientboundRulesPacket::rules,
            ClientboundRulesPacket::new
    );

    public static ClientboundRulesPacket create() {
        String ruleList = String.join("\0", Config.INSTANCE.rules);
        return new ClientboundRulesPacket(ruleList);
    }

    @NotNull
    @Override
    public CustomPacketPayload.Type<ClientboundRulesPacket> type() {
        return TYPE;
    }
}