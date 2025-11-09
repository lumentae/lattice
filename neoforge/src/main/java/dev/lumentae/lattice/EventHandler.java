package dev.lumentae.lattice;

import dev.lumentae.lattice.packet.ServerboundModSharePacket;
import dev.lumentae.lattice.util.PacketUtils;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class EventHandler {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        Event.OnServerStarted(event.getServer());
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        Event.OnServerStopping(event.getServer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Event.OnRespawn((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Event.OnJoin(((ServerPlayer) event.getEntity()).connection);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        Event.OnCommandRegister(event.getDispatcher());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        PacketUtils.sendToServer(ServerboundModSharePacket.create(event.getPlayer()));
    }

    public static void handleDataOnMain(final ServerboundModSharePacket data, final IPayloadContext context) {
        context.enqueueWork(() -> Event.OnModSharePacket(data));
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.commonToServer(
                ServerboundModSharePacket.TYPE,
                ServerboundModSharePacket.STREAM_CODEC,
                EventHandler::handleDataOnMain
        );
    }
}