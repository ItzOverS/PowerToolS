package me.overlight.powertools.bukkit.PowerModules;

public interface PowerModule {
    String[] getConfiguration();

    String getConfigName();

    String getExtensionPrefix();

    PowerModule getPowerModule();
}