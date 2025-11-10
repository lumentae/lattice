package dev.lumentae.lattice;

import dev.lumentae.lattice.decorator.DecoratorManager;
import dev.lumentae.lattice.decorator.StatusDecorator;
import dev.lumentae.lattice.decorator.TimeDecorator;
import net.minecraft.server.MinecraftServer;

public class Mod {
    private static MinecraftServer _server;
    public static boolean usesDurability = false;
    public static boolean viewingRules = false;

    public static MinecraftServer getServer() {
        return _server;
    }
    public static void setServer(MinecraftServer server) {
        _server = server;
    }

    public static void init() {
        Config.configPath.toFile().mkdirs();
        DecoratorManager.registerDecorator(new TimeDecorator());
        DecoratorManager.registerDecorator(new StatusDecorator());
        Config.loadConfig();
    }
}
