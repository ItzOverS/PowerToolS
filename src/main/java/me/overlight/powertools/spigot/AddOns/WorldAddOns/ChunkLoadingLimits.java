package me.overlight.powertools.spigot.AddOns.WorldAddOns;

import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoadingLimits
        extends AddOn
        implements Listener {
    public ChunkLoadingLimits() {
        super("WorldAddOns.ChunkLoadingLimits", "1.0", "Prevent chunk loading speed get more than a value", PowerTools.config.getBoolean("WorldAddOns.ChunkLoadingLimits.enabled"));
    }

    int chunks = 0;

    @EventHandler
    public void event(ChunkLoadEvent e) {
        chunks++;
        Bukkit.getScheduler().runTaskLater(PowerTools.INSTANCE, () -> chunks--, PowerTools.config.getInt(this.getName() + ".interval"));
        if (chunks > PowerTools.config.getInt(this.getName() + ".maxChunks")) {
            e.getChunk().unload();
        }
    }
}
