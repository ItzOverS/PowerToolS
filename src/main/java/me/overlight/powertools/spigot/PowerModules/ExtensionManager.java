package me.overlight.powertools.spigot.PowerModules;

import me.overlight.powertools.spigot.Libraries.ColorFormat;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.Plugin.PlPerms;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ExtensionManager {
    private final static List<PowerModule> extensions = new ArrayList<>();

    public static void addExtension(Plugin pl) {
        extensions.add((PowerModule) pl);
    }

    public static void removeExtension(Plugin pl) {
        PowerTools.INSTANCE.getServer().getPluginManager().disablePlugin(pl);
        extensions.remove((PowerModule) pl);
    }

    public static void removeAllExtensions() {
        for (PowerModule ext : extensions) {
            PowerTools.INSTANCE.getServer().getPluginManager().disablePlugin(PowerTools.INSTANCE.getServer().getPluginManager().getPlugin("PowerExt_" + ext.getConfigName()));
            PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.RED + "PowerToolS disabled " + ChatColor.GOLD + ext.getConfigName());
        }
        extensions.clear();
    }

    public static PowerModule getByName(String name) {
        for (PowerModule extension : extensions) {
            if (extension.getConfigName().equals(name))
                return extension;
        }
        return null;
    }

    public static void extensionAlert(PowerModule module, String message, PowerTools.Target target) {
        String msg = PlInfo.PREFIX.substring(0, PlInfo.PREFIX.length() - 1) + ChatColor.GOLD + "[" + module.getExtensionPrefix() + ChatColor.GOLD + "]" + ChatColor.RESET + " " + ((Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) ? me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(null, ColorFormat.formatColor(message)) : ColorFormat.formatColor(message));
        if (target == PowerTools.Target.CONSOLE) {
            PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage(msg);
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (target == PowerTools.Target.STAFF) {
                    if (player.hasPermission(PlPerms.Perms.Alerts.get())) {
                        player.sendMessage(msg);
                    }
                } else {
                    player.sendMessage(msg);
                }
            }
        }
    }

    public static boolean hookInto(String plName) throws IOException, ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        try {
            plName = "PowerExt_" + plName;
            Plugin pl = Bukkit.getPluginManager().getPlugin(plName);
            if (pl == null) return false;
            try {
                if (!pl.isEnabled())
                    Bukkit.getPluginManager().enablePlugin(pl);
            } catch (Exception ignored) {
            }
            Method obj = Class.forName("me.overlight.powertools." + plName.toLowerCase().substring(9) + ".PowerExt").getMethod("getPowerModule");
            PowerModule module = (PowerModule) obj.getDeclaringClass().getField("module").get(obj);
            loadConfig(module);
            if (!PowerTools.config.getBoolean(module.getConfigName() + ".enabled")) {
                PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.RED + "Extension " + ChatColor.GOLD + module.getConfigName() + ChatColor.RED + " not enabled in config.yml");
                return false;
            }
            addExtension(pl);
            PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.GREEN + "Success fully hooked into PowerExtension: " + module.getConfigName());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void loadConfig(PowerModule ext) throws IOException {
        for (String line : ext.getConfiguration()) {
            String key = line.split(":")[0].trim(),
                    value = line.split(":")[1],
                    type = line.split(":")[2];
            if (PowerTools.config.contains(ext.getConfigName() + "." + key)) continue;
            PowerTools.config.set(ext.getConfigName() + "." + key,
                    (type.equals("boolean")) ? Boolean.parseBoolean(value) :
                            (type.equals("string")) ? value :
                                    (type.equals("integer")) ? Integer.parseInt(value) :
                                            (type.equals("float")) ? Float.parseFloat(value) :
                                                    value
            );
        }
        PowerTools.config.save(new File("plugins\\PowerToolS\\config.yml"));
        FileReader reader = new FileReader("plugins\\PowerToolS\\config.yml");
        List<String> vips = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                vips.add(line);
            }
        }

    }
}
