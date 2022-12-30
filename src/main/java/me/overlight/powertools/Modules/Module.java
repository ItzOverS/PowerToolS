package me.overlight.powertools.Modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {
    private String name, description, permission, usage;
    public int startDelay, delay;
    private List<String> aliases;

    public Module(String name, String description, String permission, String usage, String[] aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.usage = usage;
        this.aliases = new ArrayList<>(Arrays.asList(aliases));
    }
    public Module(String name, String description, String permission, String usage, String[] aliases, int start, int delay) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.usage = usage;
        this.aliases = new ArrayList<>(Arrays.asList(aliases));
        this.startDelay = start;
        this.delay = delay;
    }

    public String name() {
        return name;
    }
    public String description() {
        return description;
    }
    public String permission() {
        return permission;
    }
    public String usage() {
        return usage;
    }
    public List<String> aliases() {
        return aliases;
    }
}
