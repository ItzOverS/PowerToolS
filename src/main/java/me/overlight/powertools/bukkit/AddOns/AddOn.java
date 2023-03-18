package me.overlight.powertools.bukkit.AddOns;

import me.overlight.powertools.bukkit.PowerTools;

public class AddOn {
    private final String name;
    private final String version;
    private final String description;
    private final boolean enabled;
    public PowerTools plugin;
    private String channel = null;

    public AddOn(String name, String version, String description, boolean stats) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.enabled = stats;
        this.plugin = PowerTools.INSTANCE;
        this.channel = null;
        if (stats) onEnabled();
    }

    public AddOn(String name, String version, String description, boolean stats, String channel) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.enabled = stats;
        this.plugin = PowerTools.INSTANCE;
        this.channel = channel;
        if (stats) onEnabled();
    }

    public void onEnabled() {
    }

    public void onDisabled() {
    }

    public String getName() {
        return name;
    }

    public String version() {
        return version;
    }

    public String description() {
        return description;
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
