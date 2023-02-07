package me.overlight.powertools.spigot.AddOns.Main;

import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormat
        extends AddOn
        implements Listener {
    public ChatFormat() {
        super("ChatFormat", "1.0", "Create custom chat message", PowerTools.config.getBoolean("ChatFormat.enabled"));
    }

    @EventHandler
    public void playerSendChat(AsyncPlayerChatEvent e) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            e.setFormat(ChatColor.translateAlternateColorCodes('&', me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(e.getPlayer(), PowerTools.config.getString("ChatFormat.format"))));
        else
            e.setFormat(ChatColor.translateAlternateColorCodes('&', PowerTools.config.getString("ChatFormat.format")));
    }
}
