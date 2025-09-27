package dev.lumentae.lattice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.lumentae.lattice.platform.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Config {
    /**
     * A map of player UUIDs to their play options
     * <p>
     * If a player does not have any options set, the default options will be used
     */
    public Map<UUID, PlayerPlayOptions> playerOptions = new HashMap<>();

    /** The date when the server will be opened
     * <p>
     * This is set to 24 hours from now by default
     */
    public Date serverOpenDate = Date.from(Instant.now().plusSeconds(60 * 60 * 24));

    /** The date when the end will be opened
     * <p>
     * This is set to 10 days after the server is opened
     */
    public Date endOpenDate = Date.from(serverOpenDate.toInstant().plusSeconds(60 * 60 * 24 * 10));

    /** The default play options for players
     * <p>
     * These options will be used if a player does not have any options set
     */
    public static PlayerPlayOptions DEFAULT_PLAY_OPTIONS = new PlayerPlayOptions();

    public static class PlayerPlayOptions {
        /** Whether the player is allowed to use PvP
         * <p>
         * This is true by default
         */
        public boolean enablePvP = true;
        /** Whether the player is allowed to keep their inventory on death
         * <p>
         * This is false by default
         */
        public boolean enableKeepInventory = false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Config INSTANCE = new Config();

    public static Path configPath = Services.PLATFORM.getConfigDirectory();
    public static Path configFilePath = configPath.resolve("config.json");
    public static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls()
            .create();

    public static void loadConfig() {
        try {
            // Create config file if it doesn't exist
            if (!configFilePath.toFile().exists()) {
                Files.writeString(configFilePath, gson.toJson(new Config()));
            }

            INSTANCE = gson.fromJson(Files.readString(configFilePath), Config.class);
        } catch (IOException ignored) {
        }
    }
    public static void saveConfig() {
        try {
            Files.writeString(configFilePath, gson.toJson(INSTANCE));
        } catch (IOException ignored) {
        }
    }
}