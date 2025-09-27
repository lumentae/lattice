package dev.lumentae.lattice;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.lumentae.lattice.dispenser.DispenserBehavior;
import dev.lumentae.lattice.platform.Services;
import dev.lumentae.lattice.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class Event {
    public static void OnServerStarted(MinecraftServer server) {
        Mod.setServer(server);
        ServerPlayer player = Services.PLATFORM.getFakePlayer(server);
        DispenseItemBehavior behaviors = DispenserBehavior.getDispenserBehavior(player);
        BuiltInRegistries.ITEM.forEach(item -> {
            if (DispenserBlock.DISPENSER_REGISTRY.containsKey(item)) return;
            DispenserBlock.registerBehavior(item, behaviors);
        });
    }

    public static void OnServerStopping(MinecraftServer server) {
        Mod.setServer(null);
        Config.saveConfig();
    }

    public static void OnRespawn(ServerPlayer player) {
        if (player.getLastDeathLocation().isEmpty()) {
            return;
        }
        GlobalPos o = player.getLastDeathLocation().get();
        player.sendSystemMessage(MutableComponent.create(new PlainTextContents.LiteralContents("Du bist bei "))
                .append(String.valueOf(o.pos().getX()))
                .append(" ")
                .append(String.valueOf(o.pos().getY()))
                .append(" ")
                .append(String.valueOf(o.pos().getZ()))
                .append(" gestorben!")
        );
    }

    public static void OnJoin(ServerGamePacketListenerImpl handler) {
        ServerPlayer player = handler.getPlayer();
        Config.INSTANCE.playerOptions.computeIfAbsent(player.getUUID(), k -> Config.DEFAULT_PLAY_OPTIONS);
        if (Config.INSTANCE.serverOpenDate.after(new Date()) && !player.hasPermissions(2)) {
            var reason = MutableComponent.create(new PlainTextContents.LiteralContents("Der Server ist noch nicht geöffnet!\n"))
                    .append("Der Server öffnet am ")
                    .append(MutableComponent.create(
                            new PlainTextContents.LiteralContents(Config.INSTANCE.serverOpenDate.toString())
                    ).withStyle(style -> style.withColor(ChatFormatting.GREEN)))
                    .append("!");

            ClientboundDisconnectPacket packet = new ClientboundDisconnectPacket(reason);
            handler.send(packet);
        }
    }

    public static void OnCommandRegister(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("nv")
                .executes(commandContext -> {
                    ServerPlayer player = commandContext.getSource().getPlayer();
                    assert player != null;
                    if (player.hasEffect(MobEffects.NIGHT_VISION)) {
                        player.removeEffect(MobEffects.NIGHT_VISION);
                        return Command.SINGLE_SUCCESS;
                    }
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 255, false, false));
                    return Command.SINGLE_SUCCESS;
                })
        );
        dispatcher.register(literal(Constants.MOD_ID)
                .requires(source -> source.hasPermission(2))
                .then(argument("action", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            builder.suggest("save");
                            builder.suggest("load");
                            return builder.buildFuture();

                        })
                        .executes(commandContext -> {
                            String action = StringArgumentType.getString(commandContext, "action");
                            ServerPlayer player = commandContext.getSource().getPlayer();
                            assert player != null;
                            switch (action) {
                                case "save" -> {
                                    Config.saveConfig();
                                    TextUtils.sendMessage(player, Component.literal("Config saved"));
                                    return Command.SINGLE_SUCCESS;
                                }
                                case "load" -> {
                                    Config.loadConfig();
                                    TextUtils.sendMessage(player, Component.literal("Config loaded"));
                                    return Command.SINGLE_SUCCESS;
                                }
                            }
                            commandContext.getSource().sendFailure(Component.literal("Unknown action: " + action));
                            return 0;
                        })
                )
        );

    }
}
