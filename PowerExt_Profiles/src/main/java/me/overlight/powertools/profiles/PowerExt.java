package me.overlight.powertools.profiles;

import me.overlight.powertools.PowerModules.PowerModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class PowerExt
        extends JavaPlugin
        implements PowerModule {

    public static PowerModule module;

    @Override
    public void onEnable() {
        module = this;
        getServer().getPluginCommand("profile").setExecutor(new CommandHandler());
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
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
    public PowerModule getPowerModule() {
        return this;
    }
}
