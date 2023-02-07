package me.overlight.powertools.profiles;

import me.overlight.powertools.Modules.mods.CpsMap;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Objects;

public class JoinListener
        implements Listener {
    @EventHandler
    public void event(PlayerJoinEvent e){
        ProfileManager.addProfile(e.getPlayer());
    }
    @EventHandler
    public void event(PlayerQuitEvent e){
        ProfileManager.removeProfile(e.getPlayer());
    }
}
