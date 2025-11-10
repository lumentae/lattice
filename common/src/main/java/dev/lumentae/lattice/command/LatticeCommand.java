package dev.lumentae.lattice.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.lumentae.lattice.Config;
import dev.lumentae.lattice.Constants;
import dev.lumentae.lattice.Mod;
import dev.lumentae.lattice.nickname.NicknameManager;
import dev.lumentae.lattice.status.StatusManager;
import dev.lumentae.lattice.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class LatticeCommand implements ICommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal(Constants.MOD_ID)
                .requires(source -> source.hasPermission(2))
                .then(argument("action", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            builder.suggest("config");
                            builder.suggest("status");
                            builder.suggest("nick");
                            return builder.buildFuture();
                        })
                        .then(subCommand(dispatcher))
                )
        );
    }

    public RequiredArgumentBuilder<CommandSourceStack, String> subCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        return argument("subAction", StringArgumentType.word())
                .suggests((context, builder) -> {
                    String action = StringArgumentType.getString(context, "action");
                    switch (action) {
                        case "config":
                            builder.suggest("save");
                            builder.suggest("reload");
                            break;
                        case "status", "nick":
                            for (ServerPlayer player : Mod.getServer().getPlayerList().getPlayers()) {
                                builder.suggest(player.getName().getString());
                            }
                            break;
                    }
                    return builder.buildFuture();
                })
                .then(argument("subActionArg", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            String action = StringArgumentType.getString(context, "action");
                            switch (action) {
                                case "status", "nick":
                                    builder.suggest("remove");
                                    break;
                            }
                            return builder.buildFuture();
                        })
                        .executes(commandContext -> {
                            String action = StringArgumentType.getString(commandContext, "action");
                            String subAction = StringArgumentType.getString(commandContext, "subAction");
                            String subActionArg = StringArgumentType.getString(commandContext, "subActionArg");
                            ServerPlayer player = commandContext.getSource().getPlayer();
                            ServerPlayer searchedPlayer = Mod.getServer().getPlayerList().getPlayerByName(subAction);

                            switch (action) {
                                case "status":
                                    switch (subActionArg) {
                                        case "remove":
                                            StatusManager.removeStatus(searchedPlayer);
                                            TextUtils.sendMessage(player, Component.translatable("message.lattice.status.removed"));
                                            break;
                                        default:
                                            StatusManager.setStatus(searchedPlayer, subActionArg);
                                            TextUtils.sendMessage(player, Component.translatable("message.lattice.status.set")
                                                    .append(StatusManager.getStatus(searchedPlayer)));
                                            break;
                                    }
                                    break;
                                case "nick":
                                    switch (subActionArg) {
                                        case "remove":
                                            NicknameManager.removeNickname(searchedPlayer);
                                            TextUtils.sendMessage(player, Component.translatable("message.lattice.nickname.removed"));
                                            break;
                                        default:
                                            NicknameManager.setNickname(searchedPlayer, subActionArg);
                                            TextUtils.sendMessage(player, Component.translatable("message.lattice.nickname.set")
                                                    .append(NicknameManager.getNickname(searchedPlayer)));
                                            break;
                                    }
                                    break;
                                default:
                                    TextUtils.sendMessage(player, Component.translatable("message.lattice.lattice.unknown")
                                            .append(subAction)
                                            .withStyle(ChatFormatting.RED));
                                    break;
                            }

                            return Command.SINGLE_SUCCESS;
                        })
                ).executes(commandContext -> {
                    ServerPlayer player = commandContext.getSource().getPlayer();
                    String action = StringArgumentType.getString(commandContext, "action");
                    String subAction = StringArgumentType.getString(commandContext, "subAction");

                    switch (action) {
                        case "status":
                            String status = Config.getPlayerPlayOptions(player.getUUID()).status;
                            if (status == null || status.isEmpty()) {
                                TextUtils.sendMessage(player, Component.translatable("message.lattice.status.none"));
                                return Command.SINGLE_SUCCESS;
                            }
                            TextUtils.sendMessage(player, Component.translatable("message.lattice.status.show")
                                    .append(StatusManager.getStatus(player)));
                            break;
                        case "nick":
                            String nickname = Config.getPlayerPlayOptions(player.getUUID()).nickname;
                            if (nickname == null || nickname.isEmpty()) {
                                TextUtils.sendMessage(player, Component.translatable("message.lattice.nickname.none"));
                                return Command.SINGLE_SUCCESS;
                            }
                            TextUtils.sendMessage(player, Component.translatable("message.lattice.nickname.show")
                                    .append(NicknameManager.getNickname(player)));
                            break;
                        case "config":
                            switch (subAction) {
                                case "save":
                                    Config.saveConfig();
                                    TextUtils.sendMessage(player, Component.translatable("message.lattice.config.save"));
                                    break;
                                case "reload":
                                    Config.loadConfig();
                                    TextUtils.sendMessage(player, Component.translatable("message.lattice.config.reload"));
                                    break;
                                default:
                                    TextUtils.sendMessage(player, Component.translatable("message.lattice.lattice.unknown")
                                            .append(subAction)
                                            .withStyle(ChatFormatting.RED));
                                    break;
                            }
                            break;
                        default:
                            TextUtils.sendMessage(player, Component.translatable("message.lattice.lattice.unknown")
                                    .append(subAction)
                                    .withStyle(ChatFormatting.RED));
                            break;
                    }


                    return Command.SINGLE_SUCCESS;
                })
                ;
    }
}
