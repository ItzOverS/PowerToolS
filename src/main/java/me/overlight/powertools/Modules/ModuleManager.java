package me.overlight.powertools.Modules;

import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static List<Module> modules = new ArrayList<>();
    public static void loadModulesData(){
        for(Module module: modules){
            if(module instanceof Listener)
                PowerTools.INSTANCE.getServer().getPluginManager().registerEvents((Listener)module, PowerTools.INSTANCE);
            if(module instanceof Runnable)
                Bukkit.getScheduler().runTaskTimerAsynchronously(PowerTools.INSTANCE, (Runnable) module, module.startDelay, module.delay);
            if(module instanceof CommandExecutor)
                PowerTools.INSTANCE.getServer().getPluginCommand("powertools").setExecutor((CommandExecutor) module);
        }
    }
}
