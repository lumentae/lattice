package dev.lumentae.lattice;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.lumentae.lattice.dispenser.DispenserBehavior;
import dev.lumentae.lattice.nickname.NicknameManager;
import dev.lumentae.lattice.packet.ServerboundModSharePacket;
import dev.lumentae.lattice.platform.Services;
import dev.lumentae.lattice.util.TextUtils;
import dev.lumentae.lattice.util.Utils;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

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
        player.sendSystemMessage(Component.translatable("message.lattice.death.1")
                .append(String.valueOf(o.pos().getX()))
                .append(", ")
                .append(String.valueOf(o.pos().getY()))
                .append(", ")
                .append(String.valueOf(o.pos().getZ()))
                .append(Component.translatable("message.lattice.death.2"))
        );
    }

    public static void OnJoin(ServerGamePacketListenerImpl handler) {
        ServerPlayer player = handler.getPlayer();
        Config.INSTANCE.playerOptions.computeIfAbsent(player.getUUID(), k -> Config.DEFAULT_PLAY_OPTIONS);
        if (Config.INSTANCE.serverOpenDate.after(new Date()) && !player.hasPermissions(2)) {
            var reason = Component.translatable("message.lattice.server.closed.1")
                    .append(Component.translatable("message.lattice.server.closed.2"))
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
                            builder.suggest("reload");
                            return builder.buildFuture();

                        })
                        .executes(commandContext -> {
                            String action = StringArgumentType.getString(commandContext, "action");
                            ServerPlayer player = commandContext.getSource().getPlayer();
                            assert player != null;
                            switch (action) {
                                case "save" -> {
                                    Config.saveConfig();
                                    TextUtils.sendMessage(player, Component.translatable("message.lattice.lattice.save"));
                                    return Command.SINGLE_SUCCESS;
                                }
                                case "reload" -> {
                                    Config.loadConfig();
                                    TextUtils.sendMessage(player, Component.translatable("message.lattice.lattice.reload"));
                                    return Command.SINGLE_SUCCESS;
                                }
                            }
                            TextUtils.sendMessage(player,
                                    Component.translatable("message.lattice.lattice.unknown").withStyle(ChatFormatting.RED)
                                            .append(Component.literal(action))
                            );
                            return 0;
                        })
                )
        );
        dispatcher.register(literal("nick")
                .requires(source -> source.hasPermission(2))
                .then(argument("nickname", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            ServerPlayer player = context.getSource().getPlayer();
                            assert player != null;

                            builder.suggest(player.getName().getString());
                            return builder.buildFuture();
                        })
                        .executes(commandContext -> {
                            String nickname = StringArgumentType.getString(commandContext, "nickname");

                            ServerPlayer player = commandContext.getSource().getPlayer();
                            assert player != null;

                            if (nickname.equals(player.getName().getString())) {
                                NicknameManager.removeNickname(player);
                                TextUtils.sendMessage(player, Component.translatable("message.lattice.nickname.removed").withStyle(ChatFormatting.GREEN));
                                return Command.SINGLE_SUCCESS;
                            }

                            NicknameManager.setNickname(player, nickname);
                            TextUtils.sendMessage(player, Component.translatable("message.lattice.nickname.set").append(
                                    Component.literal(nickname).withStyle(ChatFormatting.GREEN)
                            ));

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    assert player != null;

                    String name = Config.INSTANCE.nicknames.get(player.getUUID());
                    if (name == null) {
                        TextUtils.sendMessage(player, Component.translatable("message.lattice.nickname.none").withStyle(ChatFormatting.RED));
                    } else {
                        TextUtils.sendMessage(player, Component.translatable("message.lattice.nickname.show").append(
                                Component.literal(name).withStyle(ChatFormatting.GREEN)
                        ));
                    }
                    return Command.SINGLE_SUCCESS;
                })
        );
    }

    public static void OnModSharePacket(ServerboundModSharePacket packet) {
        Constants.LOG.info("Received mod/resource pack list from server:");
        Constants.LOG.info("Origin: {} ({})", packet.origin(), Utils.getPlayerNameByUUID(UUID.fromString(packet.origin())));
        Constants.LOG.info("Mods: {}", packet.mods());
        Constants.LOG.info("Resource Packs: {}", packet.resourcePacks());

        if (Utils.containsIllegalMods(packet.mods()) || Utils.containsIllegalMods(packet.resourcePacks())) {
            Constants.LOG.warn("Illegal mods or resource packs!");

            var illegalMods = new ArrayList<>(packet.mods().replace('|', '\n').lines().filter(Utils::containsIllegalMods).toList());
            illegalMods.addAll(packet.resourcePacks().replace('|', '\n').lines().filter(Utils::containsIllegalMods).toList());

            ServerPlayer player = Utils.getPlayerByUUID(UUID.fromString(packet.origin()));
            if (player != null) {
                ClientboundDisconnectPacket kickPacket = new ClientboundDisconnectPacket(
                        Component.translatable("message.lattice.illegal_mods").withStyle(ChatFormatting.RED)
                                .append(Component.literal("\n- "))
                                .append(Component.literal(String.join("\n- ", illegalMods)).withStyle(ChatFormatting.RED))
                );
                player.connection.send(kickPacket);
                player.connection.disconnect(Component.translatable("message.lattice.illegal_mods").withStyle(ChatFormatting.RED));
            }
        }
    }
}
