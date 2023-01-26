package me.overlight.powertools.profiles;

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
        InteractsLMB.put(e.getPlayer().getName(), 0);
        InteractsRMB.put(e.getPlayer().getName(), 0);
    }
    @EventHandler
    public void event(PlayerQuitEvent e){
        ProfileManager.removeProfile(e.getPlayer());
        InteractsLMB.remove(e.getPlayer().getName());
        InteractsRMB.remove(e.getPlayer().getName());
    }


    public static HashMap<String, Integer> InteractsRMB = new HashMap<>();
    public static HashMap<String, Integer> InteractsLMB = new HashMap<>();

    @EventHandler
    public void event(PlayerInteractEvent e){
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            InteractsLMB.put(e.getPlayer().getName(), InteractsLMB.get(e.getPlayer().getName()) + 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                InteractsLMB.put(e.getPlayer().getName(), InteractsLMB.get(e.getPlayer().getName()) - 1);
            }, 20);
            for(String profile: ProfileManager.profiles.keySet())
                if(Objects.equals(profile, e.getPlayer().getName()))
                    if(InteractsLMB.get(e.getPlayer().getName()) > ProfileManager.profiles.get(profile).maxLMBCps())
                        ProfileManager.profiles.get(profile).setMaxLMBCps(InteractsLMB.get(e.getPlayer().getName()));
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            InteractsRMB.put(e.getPlayer().getName(), InteractsRMB.get(e.getPlayer().getName()) + 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                InteractsRMB.put(e.getPlayer().getName(), InteractsRMB.get(e.getPlayer().getName()) - 1);
            }, 20);
            for(String profile: ProfileManager.profiles.keySet())
                if(Objects.equals(profile, e.getPlayer().getName()))
                    if(InteractsRMB.get(e.getPlayer().getName()) > ProfileManager.profiles.get(profile).maxRMBCps())
                        ProfileManager.profiles.get(profile).setMaxRMBCps(InteractsRMB.get(e.getPlayer().getName()));
        }
    }
}
