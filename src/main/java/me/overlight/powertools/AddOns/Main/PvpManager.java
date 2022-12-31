package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class PvpManager
        extends AddOn
        implements Listener {
    public PvpManager() {
        super("PvpManager", "1.0", "manage players pvp", "NONE", PowerTools.config.getBoolean("PvpManager.enabled"));
    }

    public static HashMap<String, Boolean> PlayersPvpStats = new HashMap<>();
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerDamageByPlayer(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player && e.getDamager() instanceof Player))
            return;
        if(PlayersPvpStats.containsKey(e.getEntity().getName()) && PlayersPvpStats.containsKey(e.getDamager().getName())){
            if(!PlayersPvpStats.get(e.getEntity().getName()) || !PlayersPvpStats.get(e.getDamager().getName())){
                e.setCancelled(true);
                if(!PlayersPvpStats.get(e.getDamager().getName()))
                    e.getDamager().sendMessage(PlInfo.PREFIX + ChatColor.RED + "Your pvp is disabled");
                else if(!PlayersPvpStats.get(e.getEntity().getName()))
                    e.getDamager().sendMessage(PlInfo.PREFIX + ChatColor.RED + e.getEntity().getName() + "'s pvp is disabled");
            }
        }
    }
}
