package dev.lumentae.lattice.mixin;

import dev.lumentae.lattice.Config;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    public void dropEquipment(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        UUID uuid = player.getUUID();

        // Check if the player has the keepInventory option enabled
        if (Config.INSTANCE.playerOptions.get(uuid).enableKeepInventory) {
            ci.cancel();
        }
    }
}
