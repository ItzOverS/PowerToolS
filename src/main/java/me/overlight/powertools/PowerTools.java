package me.overlight.powertools;

import com.avaje.ebeaninternal.server.cluster.Packet;
import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.Command.MainCommand;
import me.overlight.powertools.Modules.ModuleManager;
import me.overlight.powertools.Modules.mods.*;
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

        getServer().getPluginCommand("powertools").setExecutor(new MainCommand());
        getServer().getPluginCommand("powertools").setTabCompleter(new MainCommand());

        ModuleManager.registerModule(new Knockback(), new Freeze(), new Channel(), new MemoryUsage(), new Protect(), new Rotate(), new PlayTime());
        ModuleManager.loadModulesData();
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}
