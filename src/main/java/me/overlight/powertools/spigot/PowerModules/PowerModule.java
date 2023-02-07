package me.overlight.powertools.spigot.PowerModules;

public interface PowerModule {
    String[] getConfiguration();

    String getConfigName();

    String getExtensionPrefix();

    PowerModule getPowerModule();
}