package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class ForceSpawn
        extends AddOn
        implements Listener {
    public ForceSpawn() {
        super("ForceSpawn", "1.0", "Manager players spawn location", PowerTools.config.getBoolean("ForceSpawn.enabled"));
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        if (this.isEnabled()) {
            World world = Bukkit.getWorld(Objects.requireNonNull(PowerTools.config.getString(this.getName() + ".world")));
            int x = PowerTools.config.getInt(this.getName() + ".location.x"), y = PowerTools.config.getInt(this.getName() + ".location.y"), z = PowerTools.config.getInt(this.getName() + ".location.z");
            float yaw = (float) PowerTools.config.getDouble(this.getName() + ".head.yaw"), pitch = (float) PowerTools.config.getDouble(this.getName() + ".head.pitch");
            if (world == null)
                world = Bukkit.getWorlds().get(0);
            if (world == null)
                return;
            e.getPlayer().teleport(new Location(world, x, y, z, yaw, pitch));
        }
    }
}
