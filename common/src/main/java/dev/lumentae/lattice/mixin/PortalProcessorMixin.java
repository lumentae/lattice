package dev.lumentae.lattice.mixin;

import dev.lumentae.lattice.Config;
import dev.lumentae.lattice.util.TextUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PortalProcessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.portal.DimensionTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Date;

@Mixin(PortalProcessor.class)
public class PortalProcessorMixin {
    @Shadow
    private Portal portal;

    @Shadow
    private BlockPos entryPosition;

    @Inject(method = "getPortalDestination", at = @At("HEAD"), cancellable = true)
    private void init(ServerLevel level, Entity entity, CallbackInfoReturnable<DimensionTransition> cir) {
        DimensionTransition transition = this.portal.getPortalDestination(level, entity, this.entryPosition);
        assert transition != null;

        if (transition.newLevel().dimension() == Level.END && Config.INSTANCE.endOpenDate.after(new Date()) && entity instanceof ServerPlayer player) {
            TextUtils.sendMessage(player, Component.translatable("message.lattice.end_closed")
                    .append(" ")
                    .append(Component.literal(String.valueOf(Config.INSTANCE.endOpenDate))
                            .withStyle(
                                    (style) -> style.withItalic(true)
                            )
                    )
            );
            cir.setReturnValue(null);
        }
    }
}
