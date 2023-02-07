package me.overlight.powertools.playermanager;

import me.overlight.powertools.spigot.PowerModules.ExtensionManager;
import me.overlight.powertools.spigot.PowerModules.PowerModule;
import me.overlight.powertools.spigot.PowerTools;
import me.overlight.powertools.spigot.SQL.MySqlConnection;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PowerExt
        extends JavaPlugin
        implements PowerModule {

    public static PowerModule module;
    public MySqlConnection sql;
    @Override
    public void onEnable() {
        module = this;
        sql = new MySqlConnection(
                PowerTools.config.getString(PowerExt.module.getConfigName() + ".sql.username"),
                PowerTools.config.getString(PowerExt.module.getConfigName() + ".sql.password"),
                PowerTools.config.getString(PowerExt.module.getConfigName() + ".sql.address"),
                PowerTools.config.getString(PowerExt.module.getConfigName() + ".sql.port"),
                PowerTools.config.getString(PowerExt.module.getConfigName() + ".sql.dbName")
        );
        if(!sql.connect()){
            ExtensionManager.extensionAlert(this, ChatColor.RED + "Could not connect to MySQL server", PowerTools.Target.CONSOLE);
            return;
        } else {
            ExtensionManager.extensionAlert(this, ChatColor.GREEN + "Simplify connected to MySQL server", PowerTools.Target.CONSOLE);
        }
    }

    @Override
    public void onDisable() {
        sql.disconnect();
    }

    @Override
    public String[] getConfiguration() {
        return new String[]{
                "enabled:false:boolean",
            "sql.address:localhost:string",
            "sql.port:3306:string",
            "sql.dbName:minecraft:string",
            "sql.username:root:string",
            "sql.password::string",
        };
    }

    @Override
    public String getConfigName() {
        return "PlayerManager";
    }

    @Override
    public String getExtensionPrefix() {
        return ChatColor.RED + "Player" + ChatColor.AQUA + "Manager";
    }

    @Override
    public PowerModule getPowerModule() {
        return this;
    }
}
