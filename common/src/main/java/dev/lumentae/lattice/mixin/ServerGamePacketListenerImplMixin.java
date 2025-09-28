package dev.lumentae.lattice.mixin;

import dev.lumentae.lattice.Mod;
import dev.lumentae.lattice.decorator.DecoratorManager;
import dev.lumentae.lattice.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Inject(method = "broadcastChatMessage", at = @At("HEAD"), cancellable = true)
    public void broadcastChatMessage(PlayerChatMessage playerChatMessage, CallbackInfo ci) {
        assert playerChatMessage.unsignedContent() != null;
        Mod.getServer().getPlayerList().broadcastSystemMessage(
                DecoratorManager.DECORATOR.decorate(
                        Utils.getPlayerByUUID(playerChatMessage.sender()),
                        Component.literal(playerChatMessage.signedContent())
                ), false);
        this.detectRateSpam();
        ci.cancel();
    }

    @Shadow
    protected abstract void detectRateSpam();
}
