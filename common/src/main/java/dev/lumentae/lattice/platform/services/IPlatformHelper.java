package dev.lumentae.lattice.platform.services;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;
import java.util.ArrayList;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Gets the configuration directory path for the platform.
     *
     * @return The configuration directory path.
     */
    Path getConfigDirectory();

    /**
     * Gets a fake player instance for the given server.
     *
     * @param server The server to get the fake player for.
     * @return A fake player instance.
     */
    ServerPlayer getFakePlayer(MinecraftServer server);

    /**
     * Gets a list of loaded mods as strings.
     *
     * @return An ArrayList of loaded mod ids.
     */
    ArrayList<String> getModList();

    /**
     * Gets the side the code is running on (client or server).
     *
     * @return "client" if on the client side, "server" if on the server side.
     */
    String getSide();
}
