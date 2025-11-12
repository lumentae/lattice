package dev.lumentae.lattice;

import dev.lumentae.lattice.packet.ClientboundRulesPacket;
import dev.lumentae.lattice.util.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ClientEvent {
    public static void OnRulesPacket(ClientboundRulesPacket data) {
        List<String> ruleStrings = List.of(data.rules().split("\0"));
        List<Component> rules = new ArrayList<>();
        for (String rule : ruleStrings) {
            Component parsed = TextUtils.fromString(rule);
            if (parsed == null) {
                Constants.LOG.warn("Failed to parse rule: {}", rule);
                continue;
            }
            rules.add(parsed);
        }

        BookViewScreen.BookAccess bookviewscreen$bookaccess = new BookViewScreen.BookAccess(rules);
        Mod.viewingRules = true;
        Minecraft.getInstance().setScreen(new BookViewScreen(bookviewscreen$bookaccess));
    }
}
