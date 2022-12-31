package me.overlight.powertools.AddOns.Main;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class versionCheck
        extends AddOn
        implements Listener {
    public versionCheck(boolean stats) {
        super("versionCheck", "1.0", "check players version", "NONE", stats);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerJoinEvent e){
        PowerTools.Alert(PowerTools.Target.STAFF, ChatColor.GOLD + "Joined using " + PacketEvents.get().getPlayerUtils().getClientVersion(e.getPlayer()).toString().replace("v_", "").replace("_", "."));
    }
}
