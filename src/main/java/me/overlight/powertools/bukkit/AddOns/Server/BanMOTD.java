package me.overlight.powertools.bukkit.AddOns.Server;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.PowerTools;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class BanMOTD
        extends AddOn
        implements Listener {
    public BanMOTD() {
        super("ServerAddOns.BanMOTD", "1.0", "Costume MOTD for banned players", PowerTools.config.getBoolean("ServerAddOns.BanMOTD.enabled"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(ServerListPingEvent e) {
        if (Bukkit.getBanList(BanList.Type.IP).isBanned(e.getAddress().toString())) {
            e.setMotd(ChatColor.translateAlternateColorCodes('&', PowerTools.config.getString(this.getName() + ".MOTD")));
        }
    }
}
