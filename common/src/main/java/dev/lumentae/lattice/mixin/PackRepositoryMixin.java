package dev.lumentae.lattice.mixin;

import dev.lumentae.lattice.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackRepository.class)
public class PackRepositoryMixin {
    @Inject(method = "reload", at = @At("HEAD"))
    public void lattice$reload(CallbackInfo ci) {
        if (Minecraft.getInstance().player != null)
            Event.OnShareMods(Minecraft.getInstance().player);
    }
}
