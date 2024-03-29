package me.overlight.powertools.bukkit.AddOns.Main;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandRedirect
        extends AddOn
        implements Listener {
    public CommandRedirect() {
        super("CommandRedirect", "1.0", "Redirect commands", PowerTools.config.getBoolean("CommandRedirect.enabled"));
    }

    @EventHandler
    public void commandProcess(PlayerCommandPreprocessEvent e) {
        for (String key : PowerTools.config.getConfigurationSection("CommandRedirect.commands").getKeys(false)) {
            if (e.getMessage().equals("/" + key)) {
                e.setMessage(PowerTools.config.getString("CommandRedirect.commands." + key));
            }
        }
    }
}
