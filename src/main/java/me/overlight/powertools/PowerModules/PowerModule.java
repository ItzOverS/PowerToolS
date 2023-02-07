package me.overlight.powertools.PowerModules;

public interface PowerModule {
    String[] getConfiguration();

    String getConfigName();

    String getExtensionPrefix();

    PowerModule getPowerModule();
}