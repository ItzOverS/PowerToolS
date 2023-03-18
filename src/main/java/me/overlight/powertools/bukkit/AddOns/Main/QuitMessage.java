package me.overlight.powertools.bukkit.AddOns.Main;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitMessage
        extends AddOn
        implements Listener {
    public QuitMessage() {
        super("QuitMessage", "1.0", "show quit message when player left", PowerTools.config.getBoolean("QuitMessage.enabled"));
    }

    @EventHandler
    public void playerLeft(PlayerQuitEvent e) {
        if (this.isEnabled()) {
            String message = PowerTools.config.getString(this.getName() + ".message");
            assert message != null;
            message = message.replace("%NAME%", e.getPlayer().getName());
            message = message.replace("%ONLINE%", String.valueOf(Bukkit.getOnlinePlayers().size()));
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(e.getPlayer(), message)));
            else
                e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
