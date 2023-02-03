package me.overlight.powertools.AddOns.Main;

import me.clip.placeholderapi.PlaceholderAPI;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
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
    public void playerLeft(PlayerQuitEvent e){
        if(this.isEnabled()) {
            String message = PowerTools.config.getString( this.getName() + ".message");
            assert message != null;
            message = message.replace("%NAME%", e.getPlayer().getName());
            message = message.replace("%ONLINE%", String.valueOf(Bukkit.getOnlinePlayers().size()));
            e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(e.getPlayer(), message)));
        }
    }
}
