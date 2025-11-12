package dev.lumentae.lattice;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

@net.neoforged.fml.common.Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class LatticeClient {
    public LatticeClient(IEventBus eventBus) {
        NeoForge.EVENT_BUS.addListener(LatticeClient::onClientLogin);
        Config.configPath.toFile().mkdirs();
        Config.loadConfig();
    }

    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        Event.OnShareMods(event.getPlayer());
    }
}
