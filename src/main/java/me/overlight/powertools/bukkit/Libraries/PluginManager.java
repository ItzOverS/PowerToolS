package me.overlight.powertools.bukkit.Libraries;

import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginManager {
    public static void enablePlugin(String name){
        Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(name));
    }
    public static void disablePlugin(String name){
        Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(name));
    }
    public static String getEnabledPluginsAsString(){
        String activePlugins = "";
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.isEnabled()) {
                activePlugins += plugin.getName() + ", ";
            }
        }
        return activePlugins.substring(0, activePlugins.length() - 2);
    }
    public  static void enableAllPlugins(){
        for(Plugin plugin: Bukkit.getPluginManager().getPlugins()){
            if(plugin.isEnabled()) continue;
            if(plugin.getName().contains("PowerExt_")) continue;
            if(plugin == PowerTools.INSTANCE) continue;
            enablePlugin(plugin.getName());
        }
    }
    public  static void disableAllPlugins(){
        for(Plugin plugin: Bukkit.getPluginManager().getPlugins()){
            if(!plugin.isEnabled()) continue;
            if(plugin.getName().contains("PowerExt_")) continue;
            if(plugin == PowerTools.INSTANCE) continue;
            disablePlugin(plugin.getName());
        }
    }
    public static boolean isPluginValidate(String name){
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }
    public static void restartPlugin(String name){
        if(!isPluginEnabled(name)) return;
        disablePlugin(name);
        Bukkit.getScheduler().runTask(PowerTools.INSTANCE, () -> enablePlugin(name));
    }
    public static void restartAllPlugins(){
        for(Plugin plugin: Bukkit.getPluginManager().getPlugins()) {
            if (plugin == PowerTools.INSTANCE) continue;
            if (plugin.getName().contains("PowerExt_")) continue;
            if (!isPluginEnabled(plugin.getName())) return;

            disablePlugin(plugin.getName());
            Bukkit.getScheduler().runTask(PowerTools.INSTANCE, () -> enablePlugin(plugin.getName()));
        }
    }
    public static boolean isPluginEnabled(String name){
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }
}
