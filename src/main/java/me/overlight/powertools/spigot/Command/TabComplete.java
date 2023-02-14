package me.overlight.powertools.spigot.Command;

import me.overlight.powertools.spigot.Plugin.PlCommands;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class TabComplete
        implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("settings")) {
                String configLocation = mixArray(new ArrayList<>(Arrays.asList(args)), ".", 1, args.length - 1);
                FileConfiguration config = PowerTools.config;
                if (!config.contains(configLocation)) return null;
                if (!config.getConfigurationSection(configLocation).getKeys(false).isEmpty())
                    return new ArrayList<>(config.getConfigurationSection(configLocation).getKeys(false));
                YamlConfiguration types = YamlConfiguration.loadConfiguration(new File("plugins\\PowerToolS\\types.yml"));
                String type = types.getString(configLocation);
                switch (type) {
                    case "string":
                    case "stringList":
                        return Collections.singletonList("<string>");
                    case "integer":
                    case "integerList":
                        return Collections.singletonList("<integer>");
                    case "double":
                        return Collections.singletonList("<float-num>");
                    case "materialList":
                        return getMaterialList();
                    case "boolean":
                        return Arrays.asList("true", "false");
                }
                String replace = type.substring(type.indexOf("(") + 1).replace(")", "");
                if (type.startsWith("stringList(")) {
                    return Arrays.asList(replace.split(","));
                }
                if (type.startsWith("integerList(")) {
                    return getNumsIn(Integer.parseInt(replace.split(",")[0]), Integer.parseInt(replace.split(",")[1]));
                }
            } else {
                String[] commands = {
                        "pts knockback {TARGET}",
                        "pts hide:kb {TARGET}",
                        "pts knockbackall",
                        "pts hide:kba",
                        "pts freeze {TARGET}",
                        "pts hide:fr {TARGET}",
                        "pts rotate {TARGET}",
                        "pts hide:rot {TARGET}",
                        "pts playtime {TARGET}",
                        "pts hide:pt {TARGET}",
                        "pts protect {TARGET}",
                        "pts hide:prot {TARGET}",
                        "pts vanish {TARGET}",
                        "pts toggle {TOGGLE_TYPES} {TARGET}",
                        "pts blacklist add {TARGET}",
                        "pts blacklist remove {TARGET}",
                        "pts blacklist list",
                        "pts whitelist add {TARGET}",
                        "pts whitelist remove {TARGET}",
                        "pts whitelist list",
                        "pts toggle tps",
                        "pts toggle remove",
                        "pts cps {TARGET}",
                        "pts help {COMMANDS}",
                };
                Set<String> currentIndexCommands = new HashSet<>();
                for (String value : commands) {
                    if (value.split(" ").length <= args.length) continue;
                    boolean skipCommand = false;
                    for (int m = 0; m < args.length; m++) {
                        boolean itemContains = false;
                        for (String r : replaceVars(value.replace("hide:", "").split(" ")[m + 1])) {
                            if (r.startsWith(args[m])) {
                                itemContains = true;
                                break;
                            }
                        }
                        if (!itemContains)
                            skipCommand = true;
                    }
                    if (value.split(" ")[args.length].startsWith("hide:")) skipCommand = true;
                    if (skipCommand) continue;
                    currentIndexCommands.addAll(replaceVars(value.split(" ")[args.length]));
                }

                return new ArrayList<>(currentIndexCommands);
            }
        }
        return null;
    }

    public String mixArray(String[] array, String splitter) {
        String s = "";
        for (String value : array) s += value + splitter;
        return s.substring(0, s.length() - splitter.length());
    }

    private String mixArray(List<String> array, String splitter, int from, int length) {
        String main = "";
        int index = -1;
        for (String s : array) {
            index++;
            if (index >= from && index <= length + from)
                main += s + splitter;
        }
        return main.substring(0, main.length() - splitter.length());
    }

    public List<String> getMaterialList() {
        List<String> mats = new ArrayList<>();
        for (Material mat : Material.values()) {
            mats.add(mat.name());
        }
        return mats;
    }

    public List<String> getNumsIn(int start, int end) {
        List<String> i = new ArrayList<>();
        for (int m = start; m < end; m++)
            i.add(m + "");
        return i;
    }

    public List<String> replaceVars(String text) {
        if (text.equals("{TARGET}"))
            return ImplementedVariables.getOnlinePlayers();
        else if (text.equals("{TOGGLE_TYPES}"))
            return new ArrayList<>(Arrays.asList("cps", "ping"));
        else if (text.equals("{COMMANDS}"))
            return ImplementedVariables.getPluginCommands();
        return new ArrayList<>(Collections.singletonList(text));
    }

    private static class ImplementedVariables {
        public static List<String> getOnlinePlayers() {
            List<String> players = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
            return players;
        }

        public static List<String> getPluginCommands() {
            List<String> commands = new ArrayList<>();
            for (PlCommands value : PlCommands.values()) commands.add(value.getName());
            return commands;
        }
    }
}
