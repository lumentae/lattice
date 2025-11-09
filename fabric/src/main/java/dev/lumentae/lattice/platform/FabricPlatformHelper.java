package dev.lumentae.lattice.platform;

import dev.lumentae.lattice.Constants;
import dev.lumentae.lattice.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;
import java.util.ArrayList;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID);
    }

    @Override
    public ServerPlayer getFakePlayer(MinecraftServer server) {
        return FakePlayer.get(server.overworld());
    }

    @Override
    public ArrayList<String> getModList() {
        ArrayList<String> mods = new ArrayList<>();
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> {
            mods.add(modContainer.getMetadata().getId());
        });
        return mods;
    }

    @Override
    public String getSide() {
        return FabricLoader.getInstance().getEnvironmentType().toString().toLowerCase();
    }
}
