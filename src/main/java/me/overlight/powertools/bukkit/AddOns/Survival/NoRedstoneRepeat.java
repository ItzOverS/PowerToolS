package me.overlight.powertools.bukkit.AddOns.Survival;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.HashMap;

public class NoRedstoneRepeat
        extends AddOn
        implements Listener {
    public NoRedstoneRepeat() {
        super("SurvivalAddOns.NoRedstoneRepeat", "1.0", "Prevent players from creating repeaters", PowerTools.config.getBoolean("SurvivalAddOns.NoRedstoneRepeat.enabled"));
    }

    private HashMap<Location, Integer> activeTimes = new HashMap<Location, Integer>();

    @EventHandler
    public void event(BlockRedstoneEvent e) {
        if (e.getNewCurrent() - e.getOldCurrent() < 3) return;

        activeTimes.put(e.getBlock().getLocation(), activeTimes.getOrDefault(e.getBlock().getLocation(), 0) + 1);
        Bukkit.getScheduler().runTaskLater(PowerTools.INSTANCE, () -> activeTimes.put(e.getBlock().getLocation(), activeTimes.getOrDefault(e.getBlock().getLocation(), 1) - 1), PowerTools.config.getInt(this.getName() + ".interval"));

        if (activeTimes.get(e.getBlock().getLocation()) >= PowerTools.config.getInt(this.getName() + ".maxRepeat")) {
            e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation().add(0, 0.1, 0), EntityType.LIGHTNING);
            e.getBlock().setType(Material.AIR);
        }
    }
}
