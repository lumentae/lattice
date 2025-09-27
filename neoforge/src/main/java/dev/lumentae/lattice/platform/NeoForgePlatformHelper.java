package dev.lumentae.lattice.platform;

import com.mojang.authlib.GameProfile;
import dev.lumentae.lattice.Constants;
import dev.lumentae.lattice.platform.services.IPlatformHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.nio.file.Path;
import java.util.UUID;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Path getConfigDirectory() {
        return FMLLoader.getGamePath().resolve("config").resolve(Constants.MOD_ID);
    }

    @Override
    public ServerPlayer getFakePlayer(MinecraftServer server) {
        // from fabric's FakePlayer
        UUID defaultUUID = UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77");
        return new FakePlayer(server.overworld(), new GameProfile(defaultUUID, "[Minecraft]"));
    }
}
