package me.overlight.powertools.AddOns;

import me.overlight.powertools.PowerTools;

public class AddOn {
    private String name, version, description, perm;
    private boolean enabled;
    public PowerTools plugin;
    private String channel = null;
    public AddOn(String name, String version, String description, String requiredPerm, boolean stats){
        this.name = name;
        this.version = version;
        this.description = description;
        this.perm = requiredPerm;
        this.enabled = stats;
        this.plugin = PowerTools.INSTANCE;
        this.channel = null;
        if(stats) onEnabled();
    }
    public AddOn(String name, String version, String description, String requiredPerm, boolean stats, String channel){
        this.name = name;
        this.version = version;
        this.description = description;
        this.perm = requiredPerm;
        this.enabled = stats;
        this.plugin = PowerTools.INSTANCE;
        this.channel = channel;
        if(stats) onEnabled();
    }

    public void onEnabled() { }
    public void onDisabled() { }

    public String getName() {
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
    public String channel() {
        return channel;
    }
    public boolean enabled() {
        return enabled;
    }

    protected boolean isEnabled() {
        return enabled;
    }
}
