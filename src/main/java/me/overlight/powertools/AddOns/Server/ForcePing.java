package me.overlight.powertools.AddOns.Server;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.net.InetAddress;
import java.util.HashMap;

public class ForcePing
        extends AddOn
        implements Listener {
    public ForcePing() {
        super("ServerAddOns.ForcePing", "1.0", "AntiBot: Force to players ping your server before connect", "NONE", PowerTools.config.getBoolean("ServerAddOns.ForcePing.enabled"));
    }
    private final HashMap<InetAddress, Long> getLastPing = new HashMap<>();
    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        if(!getLastPing.containsKey(e.getPlayer().getAddress().getAddress()))  { kickPlayer(e.getPlayer()); return; }
        if(System.currentTimeMillis() - getLastPing.get(e.getPlayer().getAddress().getAddress()) > PowerTools.config.getLong(this.getName() + ".maxDelay")) { kickPlayer(e.getPlayer()); }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void serverPing(ServerListPingEvent e){
        getLastPing.put(e.getAddress(), System.currentTimeMillis());
    }

    private void kickPlayer(Player player){
        Bukkit.getScheduler().runTask(PowerTools.INSTANCE, new Runnable() {
            @Override
            public void run() {
                player.kickPlayer(PlInfo.KICK_PREFIX + ChatColor.RED + "You have to add our server to your server list to connect our server");
            }
        });
    }
}
