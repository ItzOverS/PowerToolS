package me.overlight.powertools.AddOns;

import me.overlight.powertools.PowerTools;
import org.bukkit.configuration.file.FileConfiguration;

public class AddOn {
    private String name, version, description, perm;
    private boolean enabled;
    public int startDelay, delay;
    public FileConfiguration config;
    public PowerTools plugin;
    public AddOn(String name, String version, String description, String requiredPerm, boolean stats){
        this.name = name;
        this.version = version;
        this.description = description;
        this.perm = requiredPerm;
        this.enabled = stats;
        this.plugin = PowerTools.INSTANCE;
        this.config = this.plugin.getConfig();
        if(stats) onEnabled();
    }
    public AddOn(String name, String version, String description, String requiredPerm, int startDelay, int delay, boolean stats){
        this.name = name;
        this.version = version;
        this.description = description;
        this.perm = requiredPerm;
        this.startDelay = startDelay;
        this.delay = delay;
        this.enabled = stats;
        this.plugin = PowerTools.INSTANCE;
        this.config = this.plugin.getConfig();
        if(stats) this.onEnabled();
    }

    public void onEnabled() { }

    public String name() {
        return name;
    }
    public String version() {
        return version;
    }
    public String description() {
        return description;
    }
    public String perm() {
        return perm;
    }
    public boolean enabled() {
        return enabled;
    }
}
