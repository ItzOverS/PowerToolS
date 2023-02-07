package me.overlight.powertools.PowerModules;

import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PluginEnabledEvent
        implements Listener {
    @EventHandler
    public void event(org.bukkit.event.server.PluginEnableEvent e) {
        if (e.getPlugin().getName().startsWith("PowerExt_")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                if (ExtensionManager.getByName(e.getPlugin().getName().substring(9)) == null)
                    PowerTools.INSTANCE.getServer().getPluginManager().disablePlugin(e.getPlugin());
            }, 1);
        }
    }
}
