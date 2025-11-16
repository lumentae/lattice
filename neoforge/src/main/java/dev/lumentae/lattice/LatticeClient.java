package dev.lumentae.lattice;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;

@net.neoforged.fml.common.Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Constants.MOD_ID)
public class LatticeClient {
    public LatticeClient(IEventBus eventBus) {
        Config.configPath.toFile().mkdirs();
        Config.loadConfig();
    }

    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        Event.OnShareMods(event.getPlayer());
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        Event.OnClientDisconnect();
    }

    @SubscribeEvent
    public static void onClientStarted(ClientStartedEvent event) {
        Constants.LOG.info("Lattice client started");
        ClientEvent.OnClientStarted(event.getClient());
    }
}
