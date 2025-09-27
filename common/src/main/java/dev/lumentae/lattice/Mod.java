package dev.lumentae.lattice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.kinds.Const;
import dev.lumentae.lattice.decorator.DecoratorManager;
import dev.lumentae.lattice.decorator.TimeDecorator;
import dev.lumentae.lattice.dispenser.DispenserBehavior;
import dev.lumentae.lattice.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.DispenserBlock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import static net.minecraft.commands.Commands.literal;

public class Mod {
    private static MinecraftServer _server;
    public static boolean usesDurability = false;

    public static MinecraftServer getServer() {
        return _server;
    }
    public static void setServer(MinecraftServer server) {
        _server = server;
    }

    public static void init() {
        Config.configPath.toFile().mkdirs();
        DecoratorManager.registerDecorator(new TimeDecorator());
        Config.loadConfig();
    }
}
