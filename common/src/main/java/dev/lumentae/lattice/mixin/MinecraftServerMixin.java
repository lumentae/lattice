package dev.lumentae.lattice.mixin;

import dev.lumentae.lattice.decorator.DecoratorManager;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Unique
    private static final ChatDecorator lattice$DECORATOR = new DecoratorManager();

    @Inject(method = "getChatDecorator", at = @At("HEAD"), cancellable = true)
    public void getDecorator(CallbackInfoReturnable<ChatDecorator> cir) {
        cir.setReturnValue(lattice$DECORATOR);
        cir.cancel();
    }
}
