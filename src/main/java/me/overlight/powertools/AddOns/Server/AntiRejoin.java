package me.overlight.powertools.AddOns.Server;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class AntiRejoin
        extends AddOn
        implements Listener {
    public AntiRejoin() {
        super("ServerAddOns.AntiRejoin", "1.0", "Prevent player from fast rejoin players", "NONE", PowerTools.config.getBoolean("ServerAddOns.AntiRejoin.enabled"));
    }

    public static HashMap<String, Long> lastDisconnect = new HashMap<>();

    @EventHandler
    public void playerQuit(PlayerQuitEvent e){
        lastDisconnect.put(e.getPlayer().getName(), System.currentTimeMillis());
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        if(lastDisconnect.containsKey(e.getPlayer().getName())){
            if(System.currentTimeMillis() - lastDisconnect.get(e.getPlayer().getName()) <= PowerTools.config.getLong(this.getName() + ".wait")){
                Bukkit.getScheduler().runTask(PowerTools.INSTANCE, () -> {
                    e.getPlayer().kickPlayer(PlInfo.KICK_PREFIX + ChatColor.RED + "Please wait before reconnect");
                });
            }
        }
    }
}
