package me.overlight.powertools.Modules;

import me.overlight.powertools.PowerTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {
    private String name, description, usage;
    public int startDelay, delay;
    private List<String> aliases;
    public PowerTools INSTANCE;

    public Module(String name, String description, String usage, String[] aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = new ArrayList<>(Arrays.asList(aliases));
        this.INSTANCE = PowerTools.INSTANCE;
    }
    public Module(String name, String description, String usage, String[] aliases, int start, int delay) {
        this.name = name;
        this.description = description;
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
    public String usage() {
        return usage;
    }
    public List<String> aliases() {
        return aliases;
    }
}
