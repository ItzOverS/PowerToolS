package me.overlight.powertools.spigot.AddOns.Server.PluginHider;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.File;

public class PluginHider
        extends AddOn
        implements Listener {

    public PluginHider() {
        super("ServerAddOns.PluginHider", "1.0", "Hide your server's plugins for plugin detector clients", PowerTools.config.getBoolean("ServerAddOns.PluginHider.enabled"));
    }

    @Override
    public void onEnabled() {
        if (PowerTools.config.getBoolean(this.getName() + ".tabComplete"))
            PacketEvents.get().registerListener(new TabCompleteListener());
        if (PowerTools.config.getBoolean(this.getName() + ".chatMessagesCheck"))
            PacketEvents.get().registerListener(new ChatMessageListener());
    }

    @EventHandler
    public void event(PlayerCommandPreprocessEvent e) {
        if (!PowerTools.config.getBoolean(this.getName() + ".disableCommand")) return;
        if (e.getMessage().equals("/pl") || e.getMessage().equals("/plugins")) {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', YamlConfiguration.loadConfiguration(new File("spigot.yml")).getString("messages.unknown-command")));
            e.setCancelled(true);
        }
    }
}
