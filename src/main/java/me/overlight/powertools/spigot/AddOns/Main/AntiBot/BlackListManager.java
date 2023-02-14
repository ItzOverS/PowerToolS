package me.overlight.powertools.spigot.AddOns.Main.AntiBot;

import me.overlight.powertools.spigot.Libraries.PluginFile;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlackListManager {
    private static List<String> username = new ArrayList<>();

    public static void blackList(Player player, String reason) {
        if (WhiteListManager.isWhitelist(player.getName())) return;
        BlackListManager.username.add(player.getName());
        PowerTools.kick(player, reason);
    }

    public static void blackList(String player) {
        BlackListManager.username.add(player);
        if (Bukkit.getPlayer(player) != null)
            PowerTools.kick(Bukkit.getPlayer(player), PlInfo.KICK_PREFIX + ChatColor.RED + "You got blacklisted by PowerAB");
    }

    public static List<String> getBlacklistedPlayers() {
        return new ArrayList<>(username);
    }

    public static void removeBlackList(String username) {
        BlackListManager.username.remove(username);
    }

    public static boolean isBlackList(String username) {
        return BlackListManager.username.contains(username);
    }

    public static boolean checkPlayer(Player player, String reason) {
        if (username.contains(player.getName())) {
            PowerTools.kick(player, reason);
            return true;
        }
        return false;
    }

    public static void save() throws IOException {
        YamlConfiguration yml = new YamlConfiguration();
        username.forEach(u -> yml.set(u, ""));
        new PluginFile("AntiBot\\blacklist").setYaml(yml).saveYaml();
    }

    public static void init() throws IOException {
        YamlConfiguration yml = new PluginFile("AntiBot\\blacklist").loadYaml().getYaml();
        username.addAll(yml.getKeys(true));
    }
}
