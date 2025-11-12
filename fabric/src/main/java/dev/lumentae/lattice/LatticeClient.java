package dev.lumentae.lattice;

import dev.lumentae.lattice.packet.ClientboundRulesPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class LatticeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            Event.OnShareMods(client.player);
        });
        ClientPlayNetworking.registerGlobalReceiver(ClientboundRulesPacket.TYPE, (payload, context) -> ClientEvent.OnRulesPacket(payload));
    }
}
