package me.overlight.powertools.bukkit.Modules.mods;

import me.overlight.powertools.bukkit.Modules.Module;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.HashMap;

public class CpsMap
        extends Module
        implements Listener {
    public CpsMap() {
        super("CpsMap", "Carefully calculate player's cps", "NONE", new String[]{});
    }

    public static HashMap<String, Integer> LMB = new HashMap<>();
    public static HashMap<String, Integer> RMB = new HashMap<>();
    public static HashMap<String, Integer> MaxLMB = new HashMap<>();
    public static HashMap<String, Integer> MaxRMB = new HashMap<>();

    @EventHandler
    public void event(PlayerInteractEvent e) {
        if (e.getPlayer() == null) return;
        if (!e.getPlayer().isOnline()) return;
        if (Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK).contains(e.getAction())) {
            LMB.put(e.getPlayer().getName(), LMB.getOrDefault(e.getPlayer().getName(), 0) + 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                if (e.getPlayer() != null && e.getPlayer().isOnline())
                    LMB.put(e.getPlayer().getName(), LMB.get(e.getPlayer().getName()) - 1);
            }, 20);
            if (LMB.get(e.getPlayer().getName()) > MaxLMB.getOrDefault(e.getPlayer().getName(), 0))
                MaxLMB.put(e.getPlayer().getName(), LMB.get(e.getPlayer().getName()));
        } else {
            RMB.put(e.getPlayer().getName(), RMB.getOrDefault(e.getPlayer().getName(), 0) + 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> { if(e.getPlayer() != null && e.getPlayer().isOnline()) RMB.put(e.getPlayer().getName(), RMB.get(e.getPlayer().getName()) - 1); }, 20);
            if (RMB.get(e.getPlayer().getName()) > MaxRMB.getOrDefault(e.getPlayer().getName(), 0))
                MaxRMB.put(e.getPlayer().getName(), RMB.get(e.getPlayer().getName()));
        }
    }

    @EventHandler
    public void event(PlayerJoinEvent e) {
        LMB.put(e.getPlayer().getName(), 0);
        RMB.put(e.getPlayer().getName(), 0);
        MaxLMB.put(e.getPlayer().getName(), 0);
        MaxRMB.put(e.getPlayer().getName(), 0);
    }

    @EventHandler
    public void event(PlayerQuitEvent e) {
        LMB.remove(e.getPlayer().getName());
        RMB.remove(e.getPlayer().getName());
    }
}
