package me.overlight.powertools.profiles;

import me.overlight.powertools.PowerModules.PowerModule;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class PowerExt
        extends JavaPlugin
        implements PowerModule {

    public static PowerModule module;
    public static HashMap<String, String> SpyPlayers = new HashMap<>();
    public static HashMap<String, Location> SpyStartLocation = new HashMap<>();

    @Override
    public void onEnable() {
        module = this;
        getServer().getPluginCommand("profile").setExecutor(new CommandHandler());
        getServer().getPluginCommand("spy").setExecutor(new SpyCommand());
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new SpyEventHandler(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public String[] getConfiguration() {
        return new String[]{
                "enabled:false:boolean"
        };
    }

    @Override
    public String getConfigName() {
        return "Profiles";
    }

    @Override
    public String getExtensionPrefix() {
        return ChatColor.GREEN + "Profiles";
    }

    @Override
    public PowerModule getPowerModule() {
        return this;
    }
}
