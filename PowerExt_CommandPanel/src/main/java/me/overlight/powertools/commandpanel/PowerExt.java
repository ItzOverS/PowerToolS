package me.overlight.powertools.commandpanel;

import me.overlight.powertools.PowerModules.ExtensionManager;
import me.overlight.powertools.PowerModules.PowerModule;
import me.overlight.powertools.PowerTools;
import org.bukkit.plugin.java.JavaPlugin;

public final class PowerExt
        extends JavaPlugin
        implements PowerModule {

    public static PowerModule module;

    @Override
    public void onEnable() {
        module = this;
        if(ExtensionManager.getByName(getConfigName()) == null) return;
        for(String key: PowerTools.config.getConfigurationSection(getConfigName() + ".panels").getKeys(false)){
            String command = PowerTools.config.getString(getConfigName() + ".panels." + key + ".command").split(" ")[0];
            getServer().getPluginCommand(command).setExecutor(new CommandExecutor());
        }
        getServer().getPluginManager().registerEvents(new InvClickEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public String[] getConfiguration() {
        return new String[]{
                "enabled:false:boolean",
                "panels.test01.command:test:string",
                "panels.test01.permission:powertools.commandpanel.test01:string",
                "panels.test01.panel.title:Test Panel By PowerToolS:string",
                "panels.test01.panel.rows:6:integer",
                "panels.test01.panel.items.fill.material:STAIRS:string",
                "panels.test01.panel.items.fill.name:mc.example.net:string",
                "panels.test01.panel.items.14.material:EMERALD:string",
                "panels.test01.panel.items.14.name:Test item:string",
                "panels.test01.panel.items.14.cmd:give @s emerald:string",
                "panels.test01.panel.items.14.cancelInteract:true:boolean",
                "panels.test01.panel.items.16.material:REDSTONE:string",
                "panels.test01.panel.items.16.name:Test 2 item:string",
                "panels.test01.panel.items.16.cmd:kill @s:string",
                "panels.test01.panel.items.16.cancelInteract:true:boolean",
        };
    }

    @Override
    public String getConfigName() {
        return "CommandPanel";
    }

    @Override
    public PowerModule getPowerModule() {
        return this;
    }
}
