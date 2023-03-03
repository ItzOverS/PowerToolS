package me.overlight.powertools.spigot.Command;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.spigot.APIs.PluginManager;
import me.overlight.powertools.spigot.AddOns.AddOnManager;
import me.overlight.powertools.spigot.AddOns.Main.AntiBot.BlackListManager;
import me.overlight.powertools.spigot.AddOns.Main.AntiBot.WhiteListManager;
import me.overlight.powertools.spigot.ConsoleMessageSaver;
import me.overlight.powertools.spigot.Libraries.AlertUtils;
import me.overlight.powertools.spigot.Libraries.ColorFormat;
import me.overlight.powertools.spigot.Libraries.MuteEntry;
import me.overlight.powertools.spigot.Libraries.RepItem;
import me.overlight.powertools.spigot.Modules.impls.Timer;
import me.overlight.powertools.spigot.Modules.mods.*;
import me.overlight.powertools.spigot.Plugin.*;
import me.overlight.powertools.spigot.PowerModules.ExtensionManager;
import me.overlight.powertools.spigot.PowerTools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainCommand
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return false;
        if (args.length > 0) {
            switch (args[0]) {
                case "help":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.Help.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(ColorFormat.formatColor("@color_gold@format_mid_line===================================================="));
                        sender.sendMessage(ColorFormat.formatColor(PlInfo.INV_PREFIX.substring(0, PlInfo.INV_PREFIX.length() - 11) + " @color_goldHelp: "));
                        int index = 1;
                        for (PlCommands item : PlCommands.values()) {
                            if (!(sender instanceof Player))
                                PowerTools.Alert(PowerTools.Target.CONSOLE, ChatColor.GRAY + "#" + index + "  " + ChatColor.GREEN + item.getName() + ChatColor.GRAY + ": " + ChatColor.DARK_GREEN + item.getDesc(), false);
                            else
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
                        if (isPlayerValid(args[1])) {
                            if(sender instanceof Player)
                                Knockback.testKnockback((Player) sender, getPlayer(args[1]));
                            else
                                PowerTools.Alert(PowerTools.Target.CONSOLE, PlMessages.OnlyPlayersCanUseCommand.get());
                        } else
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                    } else if (args.length == 3) {
                        if (!Objects.equals(args[2], "stick")) return true;
                        if (isPlayerValid(args[1])) {
                            getPlayer(args[1]).getInventory().addItem(PlSticks.KnockBackStick);
                            sender.sendMessage(PlMessages.KnockBack_StickSimplifyGiven.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                        } else {
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                        }
                    } else{
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools knockback {username}")));
                    }
                    break;
                case "knockbackall":
                case "kba":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.KnockBackCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 1) {
                        if(sender instanceof Player)
                            Knockback.testKnockbackAll((Player) sender);
                        else
                            PowerTools.Alert(PowerTools.Target.CONSOLE, PlMessages.OnlyPlayersCanUseCommand.get());
                    } else{
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools knockbackall")));
                    }
                    break;
                case "freeze":
                case "fr":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.FreezeCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 2) {
                        if (isPlayerValid(args[1])) {
                            if(sender instanceof Player)
                                Freeze.freezePlayer((Player) sender, getPlayer(args[1]));
                            else
                                PowerTools.Alert(PowerTools.Target.CONSOLE, PlMessages.OnlyPlayersCanUseCommand.get());
                        } else
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                    } else if (args.length == 3) {
                        if (!Objects.equals(args[2], "stick")) return true;
                        if (isPlayerValid(args[1])) {
                            getPlayer(args[1]).getInventory().addItem(PlSticks.FreezeStick);
                            sender.sendMessage(PlMessages.Freeze_StickSimplifyGiven.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                        } else {
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                        }
                    } else{
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools freeze {username}")));
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
                    } else if(args.length == 1){
                        if(!(sender instanceof Player)) {
                            PowerTools.Alert(PowerTools.Target.CONSOLE, PlMessages.OnlyPlayersCanUseCommand.get());
                            return true;
                        }
                        sender.sendMessage(PlMessages.CpsCheck_PlayersCpsGet.get(new RepItem("%PLAYER_NAME%", getPlayer(args[1]).getName()), new RepItem("%CPS_TYPE%", "LMB"), new RepItem("%CPS%", CpsMap.LMB.getOrDefault(sender.getName(), 0) + "")));
                        sender.sendMessage(PlMessages.CpsCheck_PlayersCpsGet.get(new RepItem("%PLAYER_NAME%", getPlayer(args[1]).getName()), new RepItem("%CPS_TYPE%", "RMB"), new RepItem("%CPS%", CpsMap.RMB.getOrDefault(sender.getName(), 0) + "")));
                    } else{
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools cps [username]")));
                    }
                    break;
                case "reload":
                case "rl":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.PluginReload.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    PowerTools.config = YamlConfiguration.loadConfiguration(new File("plugins\\PowerToolS\\config.yml"));
                    AddOnManager.unRegisterAll();
                    PowerTools.loadAddOns();
                    ExtensionManager.removeAllExtensions();
                    PowerTools.loadExtensions();
                    sender.sendMessage(PlMessages.ReloadSuccess.get());
                    break;
                case "rotate":
                case "rot":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.RotateCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 2) {
                        if (isPlayerValid(args[1])) {
                            if(sender instanceof Player)
                                Rotate.testRotate((Player) sender, getPlayer(args[1]));
                            else
                                PowerTools.Alert(PowerTools.Target.CONSOLE, PlMessages.OnlyPlayersCanUseCommand.get());
                        } else
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                    } else if (args.length == 3) {
                        if (!Objects.equals(args[2], "stick")) return true;
                        if (isPlayerValid(args[1])) {
                            getPlayer(args[1]).getInventory().addItem(PlSticks.RotateStick);
                            sender.sendMessage(PlMessages.Rotate_StickSimplifyGiven.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                        } else {
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                        }
                    } else{
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools rotate {username}")));
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
                                    ChatColor.GREEN + " play time!");
                        } else
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                    } else if(args.length == 1){
                        if(!(sender instanceof Player)) {
                            PowerTools.Alert(PowerTools.Target.CONSOLE, PlMessages.OnlyPlayersCanUseCommand.get());
                            return true;
                        }
                        Timer time = PlayTime.PlayTime.get(sender.getName());
                        sender.sendMessage(PlInfo.PREFIX +
                                ChatColor.GOLD + getPlayer(args[1]).getName() + ChatColor.GREEN + " has " +
                                ChatColor.GOLD +
                                (time.hour == 0 ? "" : time.hour + " hour" + (time.hour == 1 ? " " : "s ")) +
                                (time.minute == 0 ? "" : time.minute + " minute" + (time.minute == 1 ? " " : "s ")) +
                                (time.second == 0 ? "" : time.second + " second" + (time.second == 1 ? " " : "s ")) +
                                ChatColor.GREEN + " play time!");
                    } else{
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools playtime [username]")));
                    }
                    break;
                case "protect":
                case "prot":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.ProtectCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }

                    if (args.length == 1) {
                        if(!(sender instanceof Player)) {
                            PowerTools.Alert(PowerTools.Target.CONSOLE, PlMessages.OnlyPlayersCanUseCommand.get());
                            return true;
                        }
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
                        } else{
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                        }
                    } else{
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools protect [username]")));
                    }
                    break;
                case "vanish":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.VanishCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 1) {
                        if(!(sender instanceof Player)) {
                            PowerTools.Alert(PowerTools.Target.CONSOLE, PlMessages.OnlyPlayersCanUseCommand.get());
                            return true;
                        }
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
                        } else{
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                        }
                    } else{
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools vanish [username]")));
                    }
                    break;
                case "toggle":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.ToggleCommand.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if(args.length == 2 &&
                            args[1].equals("remove")){
                        Toggle.toggledItem.remove(sender.getName());
                        Toggle.toggledPlayers.remove(sender.getName());
                        sender.sendMessage(PlMessages.Toggle_SimplifyRemovedToggle.get());
                        return true;
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
                        } else{
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                        }
                    } else if (args.length == 2) {
                        String item = "";
                        if (args[1].equalsIgnoreCase("tps")) {
                            Toggle.toggledItem.put(sender.getName(), Toggle.ToggleItems.TPS);
                            item = "TPS";
                        } else {
                            sender.sendMessage(PlMessages.Toggle_TargetItemNotFind.get());
                            return false;
                        }
                        Toggle.toggledPlayers.put(sender.getName(), "Server");
                        sender.sendMessage(PlMessages.Toggle_SimplifySet.get(new RepItem("%TARGET_PLAYER%", "Server"), new RepItem("%TARGET_ITEM%", item)));
                    } else{
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools toggle {item} [ofPlayer]")));
                    }
                    break;/*
                case "settings":
                    if (args.length > 2) {
                        if (!PowerTools.config.getConfigurationSection(mixArray(new ArrayList<>(Arrays.asList(args)), ".", 1, args.length - 1)).getKeys(false).isEmpty()) {
                            sender.sendMessage(PlMessages.Settings_InvalidPath.get());
                            return false;
                        }
                        PowerTools.config.set(mixArray(new ArrayList<>(Arrays.asList(args)), ".", 1, args.length - 3), args[args.length - 1]);
                        sender.sendMessage(PlMessages.Settings_SuccessSetValue.get(new RepItem("%PATH%", mixArray(new ArrayList<>(Arrays.asList(args)), ".", 1, args.length - 2)), new RepItem("%VALUE%", args[args.length - 1])));
                    }
                    break;*/
                case "blacklist":
                    if (args.length == 3) {
                        switch (args[1].toLowerCase()) {
                            case "add":
                                if (!PlPerms.hasPerm(sender, PlPerms.Perms.BlackListAdd.get())) {
                                    sender.sendMessage(PlMessages.NoPermission.get());
                                    return false;
                                }
                                WhiteListManager.removeWhitelist(args[2]);
                                BlackListManager.blackList(args[2]);
                                sender.sendMessage(PlMessages.BlackList_Added.get(new RepItem("%USERNAME%", args[2])));
                                break;
                            case "remove":
                                if (!PlPerms.hasPerm(sender, PlPerms.Perms.BlackListRemove.get())) {
                                    sender.sendMessage(PlMessages.NoPermission.get());
                                    return false;
                                }
                                BlackListManager.removeBlackList(args[2]);
                                sender.sendMessage(PlMessages.BlackList_Removed.get(new RepItem("%USERNAME%", args[2])));
                                break;
                            default:
                                if (!PlPerms.hasPerm(sender, PlPerms.Perms.BlackListNotFind.get())) {
                                    sender.sendMessage(PlMessages.NoPermission.get());
                                    return false;
                                }
                                sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools blacklist {add/remove} {playerName}")));
                                break;
                        }
                    } else if (args.length == 2) {
                        if (Objects.equals(args[1], "list")) {
                            if (!PlPerms.hasPerm(sender, PlPerms.Perms.BlackListList.get())) {
                                sender.sendMessage(PlMessages.NoPermission.get());
                                return false;
                            }
                            sender.sendMessage(PlInfo.PREFIX + ChatColor.GOLD + BlackListManager.getBlacklistedPlayers());
                        }
                    } else {
                        if (!PlPerms.hasPerm(sender, PlPerms.Perms.BlackListNotFind.get())) {
                            sender.sendMessage(PlMessages.NoPermission.get());
                            return false;
                        }
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools blacklist {add/remove/list} [playerName]")));
                    }
                    break;
                case "whitelist":
                    if (args.length == 3) {
                        switch (args[1].toLowerCase()) {
                            case "add":
                                if (!PlPerms.hasPerm(sender, PlPerms.Perms.WhiteListAdd.get())) {
                                    sender.sendMessage(PlMessages.NoPermission.get());
                                    return false;
                                }
                                BlackListManager.removeBlackList(args[2]);
                                WhiteListManager.whitelist(args[2]);
                                sender.sendMessage(PlMessages.WhiteList_Added.get(new RepItem("%USERNAME%", args[2])));
                                try {
                                    WhiteListManager.save();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                break;
                            case "remove":
                                if (!PlPerms.hasPerm(sender, PlPerms.Perms.WhiteListRemove.get())) {
                                    sender.sendMessage(PlMessages.NoPermission.get());
                                    return false;
                                }
                                WhiteListManager.removeWhitelist(args[2]);
                                sender.sendMessage(PlMessages.WhiteList_Removed.get(new RepItem("%USERNAME%", args[2])));
                                try {
                                    WhiteListManager.save();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                break;
                            default:
                                if (!PlPerms.hasPerm(sender, PlPerms.Perms.WhiteListNotFind.get())) {
                                    sender.sendMessage(PlMessages.NoPermission.get());
                                    return false;
                                }
                                sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools whitelist {add/remove} {playerName}")));
                                break;
                        }
                    } else if (args.length == 2) {
                        if (Objects.equals(args[1], "list")) {
                            if (!PlPerms.hasPerm(sender, PlPerms.Perms.WhiteListList.get())) {
                                sender.sendMessage(PlMessages.NoPermission.get());
                                return false;
                            }
                            sender.sendMessage(PlInfo.PREFIX + ChatColor.GOLD + WhiteListManager.getWhitelistedPlayers());
                        }
                    } else {
                        if (!PlPerms.hasPerm(sender, PlPerms.Perms.WhiteListNotFind.get())) {
                            sender.sendMessage(PlMessages.NoPermission.get());
                            return false;
                        }
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools whitelist {add/remove/list} [playerName]")));
                    }
                    break;
                case "addons":
                    if (args.length == 1) {
                        if (!PlPerms.hasPerm(sender, PlPerms.Perms.AddOnsList.get())) {
                            sender.sendMessage(PlMessages.NoPermission.get());
                            return false;
                        }
                        String msg = "";
                        for (int i = 0; i < AddOnManager.addOns.size(); i++) {
                            String name = AddOnManager.addOns.get(i).getName().substring(AddOnManager.addOns.get(i).getName().indexOf(".") + 1);
                            String coloredName = (AddOnManager.addOns.get(i).enabled()) ? ChatColor.GREEN + name : ChatColor.RED + name;
                            if (i == AddOnManager.addOns.size() - 1) {
                                msg = msg.substring(0, msg.length() - 2);
                                msg += ChatColor.DARK_GRAY + " & " + coloredName;
                                break;
                            }
                            msg += coloredName + ChatColor.DARK_GRAY + ", ";
                        }
                        sender.sendMessage(ColorFormat.formatColor("@color_gold@format_mid_line===================================================="));
                        sender.sendMessage(msg);
                        sender.sendMessage(ColorFormat.formatColor("@color_gold@format_mid_line===================================================="));
                    } else {
                        if (!PlPerms.hasPerm(sender, PlPerms.Perms.AddOnsManage.get())) {
                            sender.sendMessage(PlMessages.NoPermission.get());
                            return false;
                        }
                        if (args.length >= 4) {
                            try {

                                String path = args[1] + "." + args[2];

                                if (!PowerTools.config.contains(path)) {
                                    sender.sendMessage(PlMessages.Settings_InvalidPath.get());
                                    return false;
                                }

                                Object obj = PowerTools.config.get(path),
                                        currentValue = PowerTools.config.get(path);

                                if (obj instanceof String) PowerTools.config.set(path, mixArray(Arrays.asList(args), " ", 3, args.length - 3));
                                else if (obj instanceof Integer) PowerTools.config.set(path, Integer.parseInt(args[3]));
                                else if (obj instanceof Long) PowerTools.config.set(path, Long.parseLong(args[3]));
                                else if (obj instanceof Boolean) PowerTools.config.set(path, Boolean.parseBoolean(args[3]));
                                else if (obj instanceof List) PowerTools.config.set(path, Arrays.asList(args).subList(3, args.length - 3));

                                PowerTools.config.save("plugins\\PowerToolS\\config.yml");
                                sender.sendMessage(PlMessages.Settings_SuccessSetValue.get(new RepItem("%PATH%", path), new RepItem("%VALUE%", args[3]), new RepItem("%FROM%", String.valueOf(currentValue))));
                            } catch (IOException e) {
                                sender.sendMessage(PlMessages.Settings_InvalidPath.get());
                            }

                        } else {
                            sender.sendMessage(PlMessages.Settings_InvalidPath.get());
                        }
                    }
                    break;
                case "mute":
                    if (args.length == 3) { // /pts mute _OverLight_ 3d
                        if (isPlayerValid(args[1])) {
                            if (!Mute.isPlayerMuted(args[1])) {
                                Mute.mutedPlayers.add(new MuteEntry(args[1], args[2], "The Mute Hammer has Spoken", Mute.currentTime()));
                                sender.sendMessage(PlMessages.Mute_UserMuted.get(new RepItem("%USERNAME%", args[1])));
                            } else {
                                sender.sendMessage(PlMessages.Mute_UserAlreadyMuted.get(new RepItem("%USERNAME%", args[1])));
                            }
                        } else
                            sender.sendMessage(PlMessages.PlayerNotFind.get());
                    } else if (args.length > 3) {
                        if (!Mute.isPlayerMuted(args[1])) {
                            Mute.mutedPlayers.add(new MuteEntry(args[1], args[2], join(new ArrayList<>(Arrays.asList(args)).subList(3, args.length - 3), " "), Mute.currentTime()));
                            sender.sendMessage(PlMessages.Mute_UserMuted.get(new RepItem("%USERNAME%", args[1])));
                        } else {
                            sender.sendMessage(PlMessages.Mute_UserAlreadyMuted.get(new RepItem("%USERNAME%", args[1])));
                        }
                    } else {
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools mute {target} {range} [reason]")));
                    }
                    break;
                case "unmute":
                    if (args.length == 2) { // /pts mute _OverLight_
                        if (Mute.isPlayerMuted(args[1])) {
                            Mute.unMute(args[1]);
                            sender.sendMessage(PlMessages.Mute_UserNoLongerMuted.get(new RepItem("%USERNAME%", args[1])));
                        } else {
                            sender.sendMessage(PlMessages.Mute_UserIsntMuted.get(new RepItem("%USERNAME%", args[1])));
                        }
                    } else {
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools unmute {target}")));
                    }
                    break;
                case "vote":
                    if (args.length == 2 &&
                            args[1].equalsIgnoreCase("create")) {
                        if (sender instanceof Player) {
                            Vote.openVoteGUI((Player) sender);
                        } else {
                            sender.sendMessage(PlMessages.OnlyPlayersCanUseCommand.get());
                        }
                    } else if (args.length == 4 &&
                            args[1].equalsIgnoreCase("give")) {
                        if (sender instanceof Player) {
                            try {
                                me.overlight.powertools.spigot.Libraries.Vote.Vote v = Vote.getVoteById(args[3]);
                                if (v == null) {
                                    sender.sendMessage(PlMessages.Vote_TargetVoteHasExpired.get());
                                    return true;
                                }
                                if (v.updateOption(Integer.parseInt(args[2]), (Player) sender)) {
                                    v.getOptions().get(Integer.parseInt(args[2])).addVoter(sender.getName());
                                    sender.sendMessage(PlMessages.Vote_SuccessfulVoted.get(new RepItem("%VOTE%", v.getOptions().get(Integer.parseInt(args[2])).getText())));
                                    Vote.openVoteStatsInventory(v, Bukkit.getPlayer(Vote.voteOwner.get(v)));
                                } else
                                    sender.sendMessage(PlMessages.Vote_AlreadyVotedFor.get());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            sender.sendMessage(PlMessages.OnlyPlayersCanUseCommand.get());
                        }
                    }
                    break;
                case "dump":
                    if (args.length == 2) {
                        switch (args[1]) {
                            case "plugin":
                                try {
                                    String dump = PlInfo.DUMP_STYLE
                                            .replace("%SERVER_VERSION%", PacketEvents.get().getServerUtils().getVersion().name() + " (" + PacketEvents.get().getServerUtils().getVersion().getProtocolVersion() + ")")
                                            .replace("%PL_VERSION%", PlInfo.VERSION).replace("%BUNGEE_CONNECTED%", String.valueOf(PowerTools.config.getBoolean("bungeecord")))
                                            .replace("%ENABLED_ADDONS%", AddOnManager.getAsString()).replace("%ENABLED_EXTENSIONS%", ExtensionManager.getAsString())
                                            .replace("%PLUGINS%", PluginManager.getEnabledPluginsAsString());

                                    sender.sendMessage(PlMessages.SimplifyCreatedDump.get(new RepItem("%URL%", "https://paste.helpch.at/" + pasteAtInternet(dump, "PowerToolS's DUMP url"))));
                                } catch (IOException ex) {
                                    sender.sendMessage(PlMessages.FailedCreateDump.get());
                                }
                                break;
                            case "console":
                                try {
                                    sender.sendMessage(PlMessages.SimplifyCreatedConsoleLog.get(new RepItem("%URL%", pasteAtInternet(ConsoleMessageSaver.getConsole(), "Server Version: " + PacketEvents.get().getServerUtils().getVersion()))));
                                } catch (IOException e) {
                                    sender.sendMessage(PlMessages.FailedCreateConsoleLog.get());
                                }
                                break;
                            case "config":
                                try {
                                    sender.sendMessage(PlMessages.SimplifyCreatedConfigDump.get(new RepItem("%URL%", pasteAtInternet(ConsoleMessageSaver.getConsole(), "PowerToolS version: " + PlInfo.VERSION))));
                                } catch (IOException e) {
                                    sender.sendMessage(PlMessages.FailedCreateConfigDump.get());
                                }
                                break;
                            default:
                                sender.sendMessage(PlMessages.DumpItemNotFind.get());
                                break;
                        }
                    }
                    break;
                case "plugins":
                case "plugin":
                case "pls":
                    if (!PlPerms.hasPerm(sender, PlPerms.Perms.Plugins.get())) {
                        sender.sendMessage(PlMessages.NoPermission.get());
                        return false;
                    }
                    if (args.length == 3) {
                        RepItem item = new RepItem("%PLUGIN_NAME%", args[2]);
                        if (!PluginManager.isPluginValidate(args[2]) && !args[2].equalsIgnoreCase("all")) {
                            sender.sendMessage(PlMessages.Plugins_PluginNotFind.get(item));
                            break;
                        }
                        switch (args[1]) {
                            case "enable":
                                if (!PlPerms.hasPerm(sender, PlPerms.Perms.Plugins_Enable.get())) {
                                    sender.sendMessage(PlMessages.NoPermission.get());
                                    return false;
                                }
                                if (!args[2].equalsIgnoreCase("all")) {
                                    if (PluginManager.isPluginEnabled(args[2]))
                                        sender.sendMessage(PlMessages.Plugins_PluginAlreadyEnabled.get(item));
                                    else {
                                        PluginManager.enablePlugin(args[2]);
                                        sender.sendMessage(PlMessages.Plugins_PluginSimplifyEnabled.get(item));
                                    }
                                } else {
                                    PluginManager.enableAllPlugins();
                                    sender.sendMessage(PlMessages.Plugins_AllPluginSimplifyEnabled.get());
                                }
                                break;
                            case "disable":
                                if (!PlPerms.hasPerm(sender, PlPerms.Perms.Plugins_Disable.get())) {
                                    sender.sendMessage(PlMessages.NoPermission.get());
                                    return false;
                                }
                                if (!args[2].equalsIgnoreCase("all")) {
                                    if (!PluginManager.isPluginEnabled(args[2]))
                                        sender.sendMessage(PlMessages.Plugins_PluginAlreadyDisabled.get(item));
                                    else {
                                        PluginManager.disablePlugin(args[2]);
                                        sender.sendMessage(PlMessages.Plugins_PluginSimplifyDisabled.get(item));
                                    }
                                } else {
                                    PluginManager.disableAllPlugins();
                                    sender.sendMessage(PlMessages.Plugins_AllPluginSimplifyDisabled.get());
                                }
                                break;
                            case "restart":
                                if (!PlPerms.hasPerm(sender, PlPerms.Perms.Plugins_Restart.get())) {
                                    sender.sendMessage(PlMessages.NoPermission.get());
                                    return false;
                                }
                                if (!args[2].equalsIgnoreCase("all")) {
                                    if (!PluginManager.isPluginEnabled(args[2]))
                                        sender.sendMessage(PlMessages.Plugins_PluginAlreadyDisabled.get(item));
                                    else {
                                        PluginManager.restartPlugin(args[2]);
                                        sender.sendMessage(PlMessages.Plugins_PluginSimplifyRestarted.get(item));
                                    }
                                } else {
                                    PluginManager.restartAllPlugins();
                                    sender.sendMessage(PlMessages.Plugins_AllPluginSimplifyRestarted.get());
                                }
                                break;
                            case "info":
                                if (!PlPerms.hasPerm(sender, PlPerms.Perms.Plugins_Info.get())) {
                                    sender.sendMessage(PlMessages.NoPermission.get());
                                    return false;
                                }
                                if (args[2].equalsIgnoreCase("all")) {
                                    sender.sendMessage(PlMessages.Plugins_ThisCommandExecuteForOnePlugin.get());
                                    break;
                                }
                                Plugin plugin = Bukkit.getPluginManager().getPlugin(args[2]);
                                String authors = null;
                                if (!plugin.getDescription().getAuthors().isEmpty()) {
                                    authors = "";
                                    if (plugin.getDescription().getAuthors().size() > 1) {
                                        for (int i = 0; i < plugin.getDescription().getAuthors().size(); i++) {
                                            if (i == plugin.getDescription().getAuthors().size() - 1) {
                                                authors = authors.substring(0, authors.length() - 2);
                                                authors += ChatColor.DARK_GRAY + " & " + plugin.getDescription().getAuthors().get(i);
                                                break;
                                            }
                                            authors += plugin.getDescription().getAuthors().get(i) + ChatColor.DARK_GRAY + ", ";
                                        }
                                    } else {
                                        authors = plugin.getDescription().getAuthors().get(0);
                                    }
                                }
                                sender.sendMessage(ColorFormat.formatColor("@color_gold@format_mid_line===================================================="));
                                sender.sendMessage(ColorFormat.formatColor(PlInfo.INV_PREFIX.substring(0, PlInfo.INV_PREFIX.length() - 11) + " @color_goldPlugin info: "));
                                sender.sendMessage(ColorFormat.formatColor("@color_green" + plugin.getName() + " @color_goldv@color_green" + plugin.getDescription().getVersion() + "@color_gold by @color_green" + ((authors == null ? "@color_redSOME ONE" : authors))));
                                sender.sendMessage(ColorFormat.formatColor("@color_goldDescription: " + (plugin.getDescription().getDescription() == null ? "@color_redDONT HAVE" : plugin.getDescription().getDescription())));
                                sender.sendMessage(ColorFormat.formatColor("@color_goldWebPage: " + (plugin.getDescription().getWebsite() == null ? "@color_redDONT HAVE" : plugin.getDescription().getWebsite())));
                                sender.sendMessage(ColorFormat.formatColor("@color_goldPlugin stats: " + (plugin.isEnabled() ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED")));
                                sender.sendMessage(ColorFormat.formatColor("@color_gold@format_mid_line===================================================="));
                                break;
                        }
                    } else {
                        sender.sendMessage(PlMessages.InvalidUsage.get(new RepItem("%CORRECT%", "/powertools plugins {enable/disable/restart/info} {pluginName}")));
                    }
                    break;
                default:
                    sender.sendMessage(PlMessages.CommandNotFind.get());
            }
        } else {
            // args.length == 0
            if (!PlPerms.hasPerm(sender, "powertools")) {
                sender.sendMessage(ColorFormat.formatColor("@color_aqua@format_mid_line=============================="));
                sender.sendMessage(ColorFormat.formatColor("@color_aquaPowerToolS v" + PlInfo.VERSION + " by ItzOverS"));
                sender.sendMessage(ColorFormat.formatColor("@color_aquaGitHub: https://github.com/ItzOverS/"));
                sender.sendMessage(ColorFormat.formatColor("@color_aqua@format_mid_line=============================="));
            } else {
                sender.sendMessage(ColorFormat.formatColor("@color_aqua@format_mid_line=============================="));
                sender.sendMessage(ColorFormat.formatColor("@color_aquaWelcome to PowerToolS v" + PlInfo.VERSION));
                sender.sendMessage(ColorFormat.formatColor("@color_aquaUse '/pts help' for information"));
                sender.sendMessage(ColorFormat.formatColor("@color_aquaPowerToolS by _OverLight_"));
                sender.sendMessage(ColorFormat.formatColor("@color_aquaGitHub: https://github.com/ItzOverS/"));
                sender.sendMessage(ColorFormat.formatColor("@color_aqua@format_mid_line=============================="));
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

    private String join(List<String> list, String splitter) {
        String main = "";
        for (String s : list) main += s + splitter;
        return main.substring(0, main.length() - splitter.length());
    }

    private String pasteAtInternet(String content, String info) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://paste.helpch.at/documents").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
        connection.setDoOutput(true);
        connection.connect();

        content =
                " /$$$$$$$                                          /$$$$$$$$                  /$$  /$$$$$$ \n" +
                        "| $$__  $$                                        |__  $$__/                 | $$ /$$__  $$\n" +
                        "| $$  \\ $$ /$$$$$$  /$$  /$$  /$$  /$$$$$$   /$$$$$$ | $$  /$$$$$$   /$$$$$$ | $$| $$  \\__/\n" +
                        "| $$$$$$$//$$__  $$| $$ | $$ | $$ /$$__  $$ /$$__  $$| $$ /$$__  $$ /$$__  $$| $$|  $$$$$$ \n" +
                        "| $$____/| $$  \\ $$| $$ | $$ | $$| $$$$$$$$| $$  \\__/| $$| $$  \\ $$| $$  \\ $$| $$ \\____  $$\n" +
                        "| $$     | $$  | $$| $$ | $$ | $$| $$_____/| $$      | $$| $$  | $$| $$  | $$| $$ /$$  \\ $$\n" +
                        "| $$     |  $$$$$$/|  $$$$$/$$$$/|  $$$$$$$| $$      | $$|  $$$$$$/|  $$$$$$/| $$|  $$$$$$/\n" +
                        "|__/      \\______/  \\_____/\\___/  \\_______/|__/      |__/ \\______/  \\______/ |__/ \\______/ " +
                        "\n" + info + "\n\n" + content;

        try (final OutputStream stream = connection.getOutputStream()) {
            stream.write(content.getBytes(StandardCharsets.UTF_8));
        }

        try (final InputStream stream = connection.getInputStream()) {
            return "https://paste.helpch.at/" + new Gson().fromJson(CharStreams.toString(new InputStreamReader(stream, StandardCharsets.UTF_8)), JsonObject.class).get("key").getAsString();
        }
    }
}
