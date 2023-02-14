package me.overlight.powertools.spigot.AddOns.Main.AntiBot;

import me.overlight.powertools.spigot.Libraries.PluginFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WhiteListManager {
    private static List<String> username = new ArrayList<>();

    public static void whitelist(Player player) {
        WhiteListManager.username.add(player.getName());
    }

    public static void whitelist(String player) {
        WhiteListManager.username.add(player);
    }

    public static void removeWhitelist(String username) {
        WhiteListManager.username.remove(username);
    }

    public static boolean isWhitelist(String username) {
        return WhiteListManager.username.contains(username);
    }


    public static List<String> getWhitelistedPlayers() {
        return new ArrayList<>(username);
    }

    public static void save() throws IOException {
        YamlConfiguration yml = new YamlConfiguration();
        username.forEach(u -> yml.set(u, ""));
        new PluginFile("AntiBot\\whitelist").setYaml(yml).saveYaml();
    }

    public static void init() throws IOException {
        YamlConfiguration yml = new PluginFile("AntiBot\\whitelist").loadYaml().getYaml();
        username.addAll(yml.getKeys(true));
    }
}
