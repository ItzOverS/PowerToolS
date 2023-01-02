package me.overlight.powertools.AddOns.Server;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.Random;

public class RandomMOTD
        extends AddOn
        implements Listener {

    public RandomMOTD() {
        super("ServerAddOns.randomMOTD", "1.1", "show random MOTDs when plugin ping", "NONE", PowerTools.config.getBoolean("ServerAddOns.randomMOTD.enabled"));
    }

    @EventHandler
    public void serverPing(ServerListPingEvent e) {
        if(this.isEnabled()){
            Random random = new Random();
            List<String> motds = PowerTools.config.getStringList(this.getName() + ".MOTDs");
            e.setMotd(ChatColor.translateAlternateColorCodes('&', motds.get(random.nextInt(motds.size()))));
        }
    }
}
