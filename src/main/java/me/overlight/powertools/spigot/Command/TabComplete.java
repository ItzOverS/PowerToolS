package me.overlight.powertools.spigot.Command;

import me.overlight.powertools.spigot.AddOns.AddOnManager;
import me.overlight.powertools.spigot.Plugin.PlCommands;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class TabComplete
        implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("addons")) {
                if (args.length < 4) {
                    if (args.length == 2) {
                        if (!args[1].contains(".")) {
                            return filter(AddOnManager.getName(), args[1]);
                        } else {
                            if (PowerTools.config.getConfigurationSection(args[1]) != null) {
                                ConfigurationSection sec = PowerTools.config.getConfigurationSection(args[1]);
                                List<String> list = new ArrayList<>();
                                for (String key : sec.getKeys(false)) {
                                    String path = Objects.requireNonNull(AddOnManager.getAddOnByName(args[1].split("\\.")[0])).getName() + "." + key;
                                    if (PowerTools.config.getConfigurationSection(path) != null)
                                        list.add(path);
                                }
                                return filter(list, args[1]);
                            }
                        }
                    } else {
                        if (PowerTools.config.getConfigurationSection(args[1]) != null) {
                            ConfigurationSection sec = PowerTools.config.getConfigurationSection(args[1]);
                            List<String> list = new ArrayList<>();
                            for (String key : sec.getKeys(false)) {
                                String path = args[1] + "." + key;
                                if (PowerTools.config.getConfigurationSection(path) == null)
                                    list.add(key);
                            }
                            return filter(list, args[2]);
                        }
                    }
                } else {
                    if (PowerTools.config.get(args[1] + "." + args[2]) instanceof List) return null;
                    else if (PowerTools.config.get(args[1] + "." + args[2]) instanceof String) return new ArrayList<>(Arrays.asList(PowerTools.config.getString(args[1] + "." + args[2]).split(" ")));
                    else return Collections.singletonList(PowerTools.config.getString(args[1] + "." + args[2]));
                }
            } else {
                String[] commands = {
                        "pts knockback {TARGET}",
                        "pts hide:kb {TARGET}",
                        "pts knockbackall",
                        "pts dump {plugin/console/config}",
                        "pts addons",
                        "pts freeze {TARGET}",
                        "pts hide:fr {TARGET}",
                        "pts rotate {TARGET}",
                        "pts hide:rot {TARGET}",
                        "pts playtime {TARGET}",
                        "pts hide:pt {TARGET}",
                        "pts protect {TARGET}",
                        "pts hide:prot {TARGET}",
                        "pts mute {TARGET}",
                        "pts unmute {TARGET}",
                        "pts vanish {TARGET}",
                        "pts toggle {cps/ping} {TARGET}",
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
                        "pts vote create",
                        "pts plugins {enable/disable/restart/info} {PLUGINS}",
                        "pts uptime",
                        "pts speed {fly/speed} {1/2/3/4/5/6/7/8/9/10}",
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
                    if (args[args.length - 1].trim().equals("")) {
                        currentIndexCommands.addAll(replaceVars(value.split(" ")[args.length]));
                    } else {
                        currentIndexCommands.addAll(replaceVars(value.split(" ")[args.length]).stream().filter(v -> v.startsWith(args[args.length - 1])).collect(Collectors.toList()));
                    }
                }

                return new ArrayList<>(currentIndexCommands);
            }
        }
        return null;
    }

    public List<String> replaceVars(String text) {
        if (text.equals("{TARGET}"))
            return ImplementedVariables.getOnlinePlayers();
        else if (text.equals("{COMMANDS}"))
            return ImplementedVariables.getPluginCommands();
        else if (text.contains("/") && text.startsWith("{") && text.endsWith("}")) {
            return new ArrayList<>(Arrays.asList(text.substring(1).substring(0, text.length() - 2).split("/")));
        } else if (text.equals("{PLUGINS}"))
            return ImplementedVariables.getPlugins();
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

        public static List<String> getPlugins() {
            List<String> plugins = new ArrayList<>();
            Arrays.stream(Bukkit.getServer().getPluginManager().getPlugins()).forEach(plugin -> plugins.add(plugin.getName()));
            return plugins;
        }
    }

    private List<String> filter(@NotNull List<String> list, String text) {
        list.removeIf(value -> !value.toLowerCase().startsWith(text.toLowerCase()));
        return list;
    }
}
