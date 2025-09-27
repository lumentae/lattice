package dev.lumentae.lattice.mixin;

import dev.lumentae.lattice.Config;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Date;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At("HEAD"), cancellable = true)
    public void teleport(ServerLevel pNewLevel, double pX, double pY, double pZ, float pYaw, float pPitch, CallbackInfo ci) {
        if (pNewLevel.dimension() == Level.END && Config.INSTANCE.endOpenDate.after(new Date())) {
            ci.cancel();
        }
    }

    @Inject(method = "canHarmPlayer", at = @At("HEAD"), cancellable = true)
    public void canHarmPlayer(Player other, CallbackInfoReturnable<Boolean> cir) {
        boolean thisPvP = Config.INSTANCE.playerOptions.get(((ServerPlayer) (Object) this).getUUID()).enablePvP;
        boolean otherPvP = Config.INSTANCE.playerOptions.get(other.getUUID()).enablePvP;

        if (!thisPvP || !otherPvP) {
            cir.setReturnValue(false);
        }
    }
}
