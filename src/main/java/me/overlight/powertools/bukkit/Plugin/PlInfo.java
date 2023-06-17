package me.overlight.powertools.bukkit.Plugin;

import me.overlight.powertools.bukkit.Libraries.ColorFormat;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.ChatColor;

public class PlInfo {
    public static String PREFIX = ChatColor.translateAlternateColorCodes('&', "&6[&l&2Power&b&lToolS&r&6]&r&f ");
    public static String INV_PREFIX = ChatColor.translateAlternateColorCodes('&', "&l&2Power&b&lToolS&r&6 >> &r&f ");
    public static String KICK_PREFIX = ChatColor.translateAlternateColorCodes('&', "&5 POWER TOOLS\n\\___________________/\n");
    public static final String DUMP_STYLE =
            "Server version: %SERVER_VERSION%\n" +
                    "Plugin version: %PL_VERSION%\n" +
                    "Operating System: %OS%\n" +
                    "Bungee Sync: %BUNGEE_CONNECTED%\n\n" +
                    "- Enabled AddOns: %ENABLED_ADDONS%\n" +
                    "- Enabled Extensions: %ENABLED_EXTENSIONS%\n" +
                    "- Plugins: %PLUGINS%\n\n";
    public static final String VERSION = "1.6";

    public static final class ADDONS {
        public static String SurvivalPrefix = ChatColor.translateAlternateColorCodes('&', "&6[&l&2Power&b&lSurvivalS&r&6]&r&f ");
    }

    public static void load() {
        if (PowerTools.messages.contains("Prefixes.Main")) PREFIX = ColorFormat.formatColor(PowerTools.messages.getString("Prefixes.Main"));
        if (PowerTools.messages.contains("Prefixes.Inventory")) INV_PREFIX = ColorFormat.formatColor(PowerTools.messages.getString("Prefixes.Inventory"));
        if (PowerTools.messages.contains("Prefixes.Kick")) KICK_PREFIX = ColorFormat.formatColor(PowerTools.messages.getString("Prefixes.Kick"));
        if (PowerTools.messages.contains("Prefixes.GameModes.Survival")) ColorFormat.formatColor(ADDONS.SurvivalPrefix = PowerTools.messages.getString("Prefixes.GameModes.Survival"));
    }
}
