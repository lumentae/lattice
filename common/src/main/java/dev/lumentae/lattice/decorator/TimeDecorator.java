package de.fynn93.servermod.decorator;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeDecorator implements ChatDecorator {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public @NotNull MutableComponent decorate(@Nullable ServerPlayer serverPlayer, @NotNull Component component) {
        String decoratedMessage = "[" + LocalDateTime.now().format(formatter) + "] ";

        return MutableComponent.create(
                new PlainTextContents.LiteralContents(decoratedMessage)
        ).withStyle(ChatFormatting.DARK_GRAY);
    }
}
