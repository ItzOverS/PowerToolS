package me.overlight.powertools;

import com.avaje.ebeaninternal.server.cluster.Packet;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class PowerTools
        extends JavaPlugin {
    public static PowerTools INSTANCE;
    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEvents.get().getSettings()
                .checkForUpdates(true)
                .bStats(true)
                .fallbackServerVersion(PacketEvents.get().getServerUtils().getVersion());
        PacketEvents.get().load();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}
