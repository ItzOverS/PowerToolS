package me.overlight.powertools.bukkit.Modules;

import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleManager {
    public static List<Module> modules = new ArrayList<>();

    public static void loadModulesData() {
        for (Module module : modules) {
            if (module instanceof Listener)
                PowerTools.INSTANCE.getServer().getPluginManager().registerEvents((Listener) module, PowerTools.INSTANCE);
            else if (module instanceof Runnable)
                Bukkit.getScheduler().runTaskTimerAsynchronously(PowerTools.INSTANCE, (Runnable) module, module.startDelay, module.delay);
            else if (module instanceof CommandExecutor)
                PowerTools.INSTANCE.getServer().getPluginCommand("powertools").setExecutor((CommandExecutor) module);
        }
    }

    public static void registerModule(Module... mods) {
        modules.addAll(new ArrayList<>(Arrays.asList(mods)));
    }
}
