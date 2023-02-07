package me.overlight.powertools.profiles;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener
        implements Listener {
    @EventHandler
    public void event(PlayerJoinEvent e) {
        ProfileManager.addProfile(e.getPlayer());
    }

    @EventHandler
    public void event(PlayerQuitEvent e) {
        ProfileManager.removeProfile(e.getPlayer());
    }
}
