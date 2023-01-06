package me.overlight.powertools.AddOns.Main;

import me.clip.placeholderapi.PlaceholderAPI;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormat
        extends AddOn
        implements Listener {
    public ChatFormat() {
        super("ChatFormat", "1.0", "Create custom chat message", "NONE", PowerTools.config.getBoolean("ChatFormat.enabled"));
    }

    @EventHandler
    public void playerSendChat(AsyncPlayerChatEvent e){
        e.setFormat(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(e.getPlayer(), PowerTools.config.getString("ChatFormat.format"))));
    }
}
