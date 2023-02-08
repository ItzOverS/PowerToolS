package me.overlight.powertools.spigot.Command;

import me.overlight.powertools.spigot.Libraries.AlertUtils;
import me.overlight.powertools.spigot.Libraries.ColorFormat;
import me.overlight.powertools.spigot.Libraries.RepItem;
import me.overlight.powertools.spigot.Modules.impls.Timer;
import me.overlight.powertools.spigot.Modules.mods.*;
import me.overlight.powertools.spigot.Plugin.*;
import me.overlight.powertools.spigot.PowerTools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainCommand
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "help":
                    if (args.length == 1) {
                        sender.sendMessage(ColorFormat.formatColor("@color_gold@format_mid_line===================================================="));
                        sender.sendMessage(ColorFormat.formatColor(PlInfo.INV_PREFIX.substring(0, PlInfo.INV_PREFIX.length() - 11) + " @color_goldHelp: "));
                        int index = 0;
                        for (PlCommands item : PlCommands.values()) {
                            AlertUtils.sendHoverClickableMessage((Player) sender, ChatColor.GRAY + "#" + index + "  " + ChatColor.GREEN + item.getName(), new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, item.getUsage()), new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(item.getDesc() + "\n\n" + ChatColor.AQUA + "Click to use it in your chat").create()));
                            index++;
                        }
                        sender.sendMessage(ColorFormat.formatColor("@color_gold@format_mid_line===================================================="));
                    } else if (args.length == 2) {
                        PlCommands cmd = null;
                        try {
                            cmd = PlCommands.valueOf(args[1]);
                        } catch (IllegalArgumentException ex) {
                            sender.sendMessage(PlMessages.HelpCMD_CommandNotFind.get());
                            return true;
                        }
                        sender.sendMessage(ColorFormat.formatColor("@color_gold@format_mid_line===================================================="));
                        sender.sendMessage(ColorFormat.formatColor(PlInfo.INV_PREFIX.substring(0, PlInfo.INV_PREFIX.length() - 11) + " @color_goldCommand help: "));
                        sender.sendMessage(ColorFormat.formatColor("@color_goldCommand name: " + cmd.getName()));
                        sender.sendMessage(ColorFormat.formatColor("@color_goldCommand description: " + cmd.getDesc()));
                        sender.sendMessage(ColorFormat.formatColor("@color_goldCommand usage: " + cmd.getUsage()));
                        sender.sendMessage(ColorFormat.formatColor("@color_goldCommand permission: " + cmd.getPermission()));
                        sender.sendMessage(ColorFormat.formatColor("@color_gold@format_mid_line===================================================="));
                    }
                    break;
                case "knockback":
                case "kb":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.KnockBackCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 2) {
                        if (isPlayerValid(args[1]))
                            Knockback.testKnockback((Player) sender, getPlayer(args[1]));
                        else
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                    } else if (args.length == 3) {
                        if (!Objects.equals(args[2], "stick")) return true;
                        if (isPlayerValid(args[1])) {
                            getPlayer(args[1]).getInventory().addItem(PlSticks.KnockBackStick);
                            sender.sendMessage(PlMessages.KnockBack_StickSimplifyGiven.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                        } else {
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                        }
                    }
                    break;
                case "freeze":
                case "fr":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.FreezeCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 2) {
                        if (isPlayerValid(args[1]))
                            Freeze.freezePlayer((Player) sender, getPlayer(args[1]));
                        else
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                    } else if (args.length == 3) {
                        if (!Objects.equals(args[2], "stick")) return true;
                        if (isPlayerValid(args[1])) {
                            getPlayer(args[1]).getInventory().addItem(PlSticks.FreezeStick);
                            sender.sendMessage(PlMessages.Freeze_StickSimplifyGiven.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                        } else {
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                        }
                    }
                    break;
                case "cps":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.CpsCheckCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 2) {
                        if (isPlayerValid(args[1])) {
                            sender.sendMessage(PlMessages.CpsCheck_PlayersCpsGet.get(new RepItem("%PLAYER_NAME%", getPlayer(args[1]).getName()), new RepItem("%CPS_TYPE%", "LMB"), new RepItem("%CPS%", CpsMap.LMB.getOrDefault(getPlayer(args[1]).getName(), 0) + "")));
                            sender.sendMessage(PlMessages.CpsCheck_PlayersCpsGet.get(new RepItem("%PLAYER_NAME%", getPlayer(args[1]).getName()), new RepItem("%CPS_TYPE%", "RMB"), new RepItem("%CPS%", CpsMap.RMB.getOrDefault(getPlayer(args[1]).getName(), 0) + "")));
                        } else {
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                        }
                    }
                    break;
                case "reload":
                case "rl":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.PluginReload.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    PowerTools.config = PowerTools.INSTANCE.getConfig();
                    sender.sendMessage(PlMessages.ReloadSuccess.get());
                    break;
                case "rotate":
                case "rot":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.RotateCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 2) {
                        if (isPlayerValid(args[1]))
                            Rotate.testRotate((Player) sender, getPlayer(args[1]));
                        else
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                    } else if (args.length == 3) {
                        if (!Objects.equals(args[2], "stick")) return true;
                        if (isPlayerValid(args[1])) {
                            getPlayer(args[1]).getInventory().addItem(PlSticks.RotateStick);
                            sender.sendMessage(PlMessages.Rotate_StickSimplifyGiven.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                        } else {
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                        }
                    }
                    break;
                case "playtime":
                case "pt":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.PlayTimeCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 2) {
                        if (isPlayerValid(args[1])) {
                            Timer time = PlayTime.PlayTime.get(getPlayer(args[1]).getName());
                            sender.sendMessage(PlInfo.PREFIX +
                                    ChatColor.GOLD + getPlayer(args[1]).getName() + ChatColor.GREEN + " has " +
                                    ChatColor.GOLD +
                                    (time.hour == 0 ? "" : time.hour + " hour" + (time.hour == 1 ? " " : "s ")) +
                                    (time.minute == 0 ? "" : time.minute + " minute" + (time.minute == 1 ? " " : "s ")) +
                                    (time.second == 0 ? "" : time.second + " second" + (time.second == 1 ? " " : "s ")) +
                                    ChatColor.GREEN + " playTime!");
                        } else
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                    }
                    break;
                case "protect":
                case "prot":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.ProtectCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }

                    if (args.length == 1) {
                        if (Protect.protectedPlayers.contains(sender.getName())) {
                            sender.sendMessage(PlMessages.Protect_YouAreNoLongerProtected.get());
                            Protect.protectedPlayers.remove(sender.getName());
                        } else {
                            sender.sendMessage(PlMessages.Protect_YouAreNowProtected.get());
                            Protect.protectedPlayers.add(sender.getName());
                        }
                    } else if (args.length == 2) {
                        if (isPlayerValid(args[1])) {
                            if (Protect.protectedPlayers.contains(sender.getName())) {
                                sender.sendMessage(PlMessages.Protect_PlayerIsNoLongerProtected.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                                Protect.protectedPlayers.remove(getPlayer(args[1]).getName());
                            } else {
                                sender.sendMessage(PlMessages.Protect_PlayerIsNowProtected.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                                Protect.protectedPlayers.add(getPlayer(args[1]).getName());
                            }
                        }
                    }
                    break;
                case "vanish":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.VanishCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 1) {
                        if (Vanish.vanishedPlayers.contains(((Player) sender).getUniqueId())) {
                            sender.sendMessage(PlMessages.Vanish_YouAreNoLongerVanish.get());
                            Vanish.vanishPlayer(Bukkit.getPlayer(sender.getName()));
                        } else {
                            sender.sendMessage(PlMessages.Vanish_YouAreNowVanish.get());
                            Vanish.vanishPlayer(Bukkit.getPlayer(sender.getName()));
                        }
                    } else if (args.length == 2) {
                        if (isPlayerValid(args[1])) {
                            if (Vanish.vanishedPlayers.contains(getPlayer(args[1]).getUniqueId())) {
                                sender.sendMessage(PlMessages.Vanish_PlayerIsNoLongerVanish.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                                Vanish.vanishPlayer(getPlayer(args[1]));
                            } else {
                                sender.sendMessage(PlMessages.Vanish_PlayerIsNowVanish.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                                Vanish.vanishPlayer(getPlayer(args[1]));
                            }
                        }
                    }
                    break;
                case "toggle":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.ToggleCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 3) {
                        if (isPlayerValid(args[2])) {
                            String item = "";
                            switch (args[1].toLowerCase()) {
                                case "cps":
                                    Toggle.toggledItem.put(sender.getName(), Toggle.ToggleItems.CPS);
                                    item = "CPS";
                                    break;
                                case "ping":
                                    Toggle.toggledItem.put(sender.getName(), Toggle.ToggleItems.PING);
                                    item = "PING";
                                    break;
                                default:
                                    sender.sendMessage(PlMessages.Toggle_TargetItemNotFind.get());
                                    return false;
                            }
                            Toggle.toggledPlayers.put(sender.getName(), getPlayer(args[2]).getName());
                            sender.sendMessage(PlMessages.Toggle_SimplifySet.get(new RepItem("%TARGET_PLAYER%", getPlayer(args[2]).getName()), new RepItem("%TARGET_ITEM%", item)));
                        }
                    } else if (args.length == 2) {
                        String item = "";
                        if (args[1].toLowerCase().equals("tps")) {
                            Toggle.toggledItem.put(sender.getName(), Toggle.ToggleItems.TPS);
                            item = "TPS";
                        } else {
                            sender.sendMessage(PlMessages.Toggle_TargetItemNotFind.get());
                            return false;
                        }
                        Toggle.toggledPlayers.put(sender.getName(), "Server");
                        sender.sendMessage(PlMessages.Toggle_SimplifySet.get(new RepItem("%TARGET_PLAYER%", "Server"), new RepItem("%TARGET_ITEM%", item)));
                    }
                    break;
                case "settings":
                    if (args.length > 2) {
                        if (!PowerTools.config.getConfigurationSection(mixArray(new ArrayList<>(Arrays.asList(args)), ".", 1, args.length - 1)).getKeys(false).isEmpty()) {
                            sender.sendMessage(PlMessages.Settings_InvalidPath.get());
                            return false;
                        }
                        PowerTools.config.set(mixArray(new ArrayList<>(Arrays.asList(args)), ".", 1, args.length - 1), args[args.length - 1]);
                        sender.sendMessage(PlMessages.Settings_SuccessSetValue.get(new RepItem("%PATH%", mixArray(new ArrayList<>(Arrays.asList(args)), ".", 1, args.length - 2)), new RepItem("%VALUE%", args[args.length - 1])));
                    }
                    break;
                default:
                    sender.sendMessage(PlMessages.CommandNotFind.get());
            }
        } else {
            // args.length == 0
            if (!PlPerms.hasPerm(sender, "powertools")) {
                sender.sendMessage(ColorFormat.formatColor("@color_aquaWelcome to PowerToolS v" + PlInfo.VERSION));
            } else {
                sender.sendMessage(ColorFormat.formatColor("@color_aquaWelcome to PowerToolS v" + PlInfo.VERSION));
                sender.sendMessage(ColorFormat.formatColor("@color_aquaUse '/pts help' for information"));
            }
        }

        return true;
    }

    public boolean isPlayerValid(String username) {
        return Bukkit.getPlayer(username) != null;
    }

    public Player getPlayer(String username) {
        return Bukkit.getPlayer(username);
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
}