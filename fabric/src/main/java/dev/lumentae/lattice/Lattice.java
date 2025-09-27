package dev.lumentae.lattice;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class Lattice implements ModInitializer {
    @Override
    public void onInitialize() {
        Mod.init();

        ServerLifecycleEvents.SERVER_STARTED.register(Event::OnServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(Event::OnServerStopping);
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            Event.OnRespawn(newPlayer);
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            Event.OnJoin(handler);
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            Event.OnCommandRegister(dispatcher);
        });
    }
}
