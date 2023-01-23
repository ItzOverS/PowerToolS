package me.overlight.powertools.PowerModules;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;

public interface PowerModule {
    String[] getConfiguration();
    String getConfigName();
    PowerModule getPowerModule();
}