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

    @EventHandler
    public void event(PlayerInteractEvent e){
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            CpsMap.LMB.put(e.getPlayer().getName(), CpsMap.LMB.get(e.getPlayer().getName()) + 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                CpsMap.LMB.put(e.getPlayer().getName(), CpsMap.LMB.get(e.getPlayer().getName()) - 1);
            }, 20);
            for(String profile: ProfileManager.profiles.keySet())
                if(Objects.equals(profile, e.getPlayer().getName()))
                    if(CpsMap.MaxLMB.get(e.getPlayer().getName()) > ProfileManager.profiles.get(profile).maxLMBCps())
                        ProfileManager.profiles.get(profile).setMaxLMBCps(CpsMap.MaxLMB.get(e.getPlayer().getName()));
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            CpsMap.RMB.put(e.getPlayer().getName(), CpsMap.RMB.get(e.getPlayer().getName()) + 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                CpsMap.RMB.put(e.getPlayer().getName(), CpsMap.RMB.get(e.getPlayer().getName()) - 1);
            }, 20);
            for(String profile: ProfileManager.profiles.keySet())
                if(Objects.equals(profile, e.getPlayer().getName()))
                    if(CpsMap.MaxRMB.get(e.getPlayer().getName()) > ProfileManager.profiles.get(profile).maxRMBCps())
                        ProfileManager.profiles.get(profile).setMaxRMBCps(CpsMap.MaxRMB.get(e.getPlayer().getName()));
        }
    }
}
