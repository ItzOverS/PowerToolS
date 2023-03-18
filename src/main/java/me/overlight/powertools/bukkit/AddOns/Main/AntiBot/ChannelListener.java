package me.overlight.powertools.bukkit.AddOns.Main.AntiBot;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;

public class ChannelListener
        implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if (!s.equals("pts:ab:bungee")) return;
        String content = new String(bytes, StandardCharsets.UTF_8);
    }
}
