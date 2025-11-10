package dev.lumentae.lattice;

import dev.lumentae.lattice.packet.ServerboundModSharePacket;
import dev.lumentae.lattice.util.PacketUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@net.neoforged.fml.common.Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class LatticeClient {
    public LatticeClient(IEventBus eventBus) {
        Mod.init();
    }

    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        PacketUtils.sendToServer(ServerboundModSharePacket.create(event.getPlayer()));
    }
}
