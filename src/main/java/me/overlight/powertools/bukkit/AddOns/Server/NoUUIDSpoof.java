package me.overlight.powertools.bukkit.AddOns.Server;

import me.overlight.powertools.bukkit.APIs.NetworkChecker;
import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.Plugin.PlInfo;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NoUUIDSpoof
        extends AddOn
        implements Listener {
    public NoUUIDSpoof() {
        super("ServerAddOns.NoUUIDSpoof", "1.0", "Prevent players from spoofing they uuid in your server", PowerTools.config.getBoolean("ServerAddOns.NoUUIDSpoof.enabled") && Bukkit.getOnlineMode());
    }

    @EventHandler
    public void event(PlayerJoinEvent e) {
        try {
            if (!e.getPlayer().getUniqueId().toString().equals(NetworkChecker.getPremiumPlayerUUID(e.getPlayer())))
                e.getPlayer().kickPlayer(PlInfo.KICK_PREFIX + ChatColor.RED + "UUID Spoof Detected");
        } catch (Exception ex) {
        }
    }
}
