package me.overlight.powertools.spigot.AddOns.Server.PluginHider;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.spigot.AddOns.AddOn;
import org.bukkit.event.Listener;

public class PluginHider
        extends AddOn
        implements Listener {

    public PluginHider() {
        super("ServerAddOns.PluginHider", "1.0", "Hide your server's plugins for plugin detector clients", true);
    }

    @Override
    public void onEnabled() {
        PacketEvents.get().registerListener(new TabCompleteListener());
    }
}
