package me.overlight.powertools.PowerModules;

import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtensionManager {
    private final static List<PowerModule> extensions = new ArrayList<>();

    public static void addExtension(Plugin pl){
        extensions.add((PowerModule) pl);
    }
    public static void removeExtension(Plugin pl){
        extensions.remove((PowerModule) pl);
    }
    public static void removeAllExtensions(){
        extensions.clear();
    }

    public static PowerModule getByName(String name){
        for (PowerModule extension : extensions) {
            if(extension.getConfigName().equals(name))
                return extension;
        }
        return null;
    }

    public static boolean hookInto(String plName) throws IOException, ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        try {
            Plugin pl = Bukkit.getPluginManager().getPlugin(plName);
            if (pl == null) return false;
            if (!pl.isEnabled()) Bukkit.getPluginManager().enablePlugin(pl);
            Method obj = Class.forName("me.overlight.powertools." + plName.toLowerCase().substring(9) + ".PowerExt").getMethod("getPowerModule");
            PowerModule module = (PowerModule) obj.getDeclaringClass().getField("module").get(obj);
            addExtension(pl);
            loadConfig(module);
            PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.GREEN + "Success fully hooked into PowerExtension: " + module.getConfigName());
            return true;
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public static void loadConfig(PowerModule ext) throws IOException {
        for(String line: ext.getConfiguration()){
            String key = line.split(":")[0].trim(),
                    value = line.split(":")[1],
                    type = line.split(":")[2];
            if(PowerTools.config.contains(ext.getConfigName() + "." + key)) continue;
            PowerTools.config.set(ext.getConfigName() + "." + key,
                (type.equals("boolean"))? Boolean.parseBoolean(value):
                        (type.equals("string"))? value:
                                (type.equals("integer"))? Integer.parseInt(value):
                                        (type.equals("float"))? Float.parseFloat(value):
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
