package me.overlight.powertools.bukkit.AddOns.Hub;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VoidTP
        extends AddOn
        implements Listener {
    public VoidTP() {
        super("HubAddOns.VoidTP", "1.0", "Teleport back to spawn when get into void", PowerTools.config.getBoolean("HubAddOns.VoidTP.enabled"));
    }

    @EventHandler
    public void eventMove(PlayerMoveEvent e) {
        if (e.getPlayer().getLocation().getY() <= PowerTools.config.getDouble("teleportY")) {
            e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), PowerTools.config.getDouble("respawnLocation.x"), PowerTools.config.getDouble("respawnLocation.y"), PowerTools.config.getDouble("respawnLocation.z")));
        }
    }
}
