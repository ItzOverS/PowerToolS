package me.overlight.powertools.spigot.Plugin;

import org.bukkit.ChatColor;

public class PlInfo {
    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&6[&l&2Power&b&lToolS&r&6]&r&f ");
    public static final String INV_PREFIX = ChatColor.translateAlternateColorCodes('&', "&l&2Power&b&lToolS&r&6 >> &r&f ");
    public static final String KICK_PREFIX = ChatColor.translateAlternateColorCodes('&', "&5 POWER TOOLS\n\\___________________/\n");
    public static final String DUMP_STYLE =
            "Server version: %SERVER_VERSION%\n" +
                    "Plugin version: %PL_VERSION%\n" +
                    "Operating System: %OS%\n" +
                    "Bungee Sync: %BUNGEE_CONNECTED%\n\n" +
                    "- Enabled AddOns: %ENABLED_ADDONS%\n" +
                    "- Enabled Extensions: %ENABLED_EXTENSIONS%\n" +
                    "- Plugins: %PLUGINS%\n\n";
    public static final String VERSION = "1.5";

    public static final class ADDONS {
        public static final String SurvivalPrefix = ChatColor.translateAlternateColorCodes('&', "&6[&l&2Power&b&lSurvivalS&r&6]&r&f ");
    }
}
