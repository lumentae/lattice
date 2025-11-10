package dev.lumentae.lattice.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.lumentae.lattice.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StrictJsonParser;

public class TextUtils {
    public static void sendMessage(ServerPlayer player, MutableComponent message) {
        MutableComponent startComponent =
                createGradient("[" + Constants.MOD_NAME +  "]", 0xFF5555, 0xFFAA00, true)
                .append(Component.literal(" ")).withStyle(ChatFormatting.RESET)
                .append(Component.literal("» ").withStyle(ChatFormatting.GRAY));

        player.sendSystemMessage(startComponent.append(message));
    }

    public static MutableComponent createGradient(String text, int startColor, int endColor, boolean bold) {
        MutableComponent result = Component.empty();

        int length = text.length();
        if (length == 0) {
            return result;
        }

        int startR = (startColor >> 16) & 0xFF;
        int startG = (startColor >> 8) & 0xFF;
        int startB = startColor & 0xFF;

        int endR = (endColor >> 16) & 0xFF;
        int endG = (endColor >> 8) & 0xFF;
        int endB = endColor & 0xFF;

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);

            float t = (length == 1) ? 0.0f : (float) i / (float) (length - 1);

            int r = (int) (startR + t * (endR - startR));
            int g = (int) (startG + t * (endG - startG));
            int b = (int) (startB + t * (endB - startB));

            int rgb = (r << 16) | (g << 8) | b;

            MutableComponent part = Component.literal(String.valueOf(c))
                    .withStyle(Style.EMPTY.withColor(rgb));

            if (bold) {
                part = part.withStyle(ChatFormatting.BOLD);
            }

            result.append(part);
        }

        return result;
    }

    public static Component fromString(String text) {
        JsonElement jsonelement = StrictJsonParser.parse(text);
        return ComponentSerialization.CODEC
                .parse(RegistryAccess.EMPTY.createSerializationContext(JsonOps.INSTANCE), jsonelement)
                .resultOrPartial(parsed -> Constants.LOG.warn("Failed to parse component '{}': {}", text, parsed))
                .orElse(null);
    }

    public static Component parseColoredText(String text) {
        if (text.isEmpty()) {
            return Component.literal("");
        }

        // Remove surrounding quotes if present
        if ((text.startsWith("\"") && text.endsWith("\"")) || (text.startsWith("'") && text.endsWith("'"))) {
            text = text.substring(1, text.length() - 1);
        }

        Component result = fromString(text);
        if (result == null) {
            result = Component.literal(text);
        }
        return result;
    }
}
