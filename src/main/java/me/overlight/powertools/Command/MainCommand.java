package me.overlight.powertools.Command;

import me.overlight.powertools.Libraries.RepItem;
import me.overlight.powertools.Modules.impls.Timer;
import me.overlight.powertools.Modules.mods.*;
import me.overlight.powertools.Modules.mods.Freeze;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.Plugin.PlMessages;
import me.overlight.powertools.Plugin.PlPerms;
import me.overlight.powertools.Plugin.PlSticks;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MainCommand
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch(args[0]) {
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
                if(args.length == 2){
                    if(isPlayerValid(args[1])){
                        sender.sendMessage(PlMessages.CpsCheck_PlayersCpsGet.get(new RepItem("%PLAYER_NAME%", args[1]), new RepItem("%CPS_TYPE%", "LMB"), new RepItem("%CPS%", CpsMap.LMB.get(args[1]) + "")));
                        sender.sendMessage(PlMessages.CpsCheck_PlayersCpsGet.get(new RepItem("%PLAYER_NAME%", args[1]), new RepItem("%CPS_TYPE%", "RMB"), new RepItem("%CPS%", CpsMap.RMB.get(args[1]) + "")));
                    } else{
                        sender.sendMessage(PlMessages.PlayerNotFind.get());
                    }
                }
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
                        Timer time = PlayTime.PlayTime.get(args[1]);
                        sender.sendMessage(PlInfo.PREFIX +
                                ChatColor.GOLD + args[1] + ChatColor.GREEN + " has " +
                                ChatColor.GOLD +
                                (time.hour == 0 ? "" : time.hour + " hour" + (time.hour == 1 ? " " : "s ")) +
                                (time.minute == 0 ? "" : time.minute + " minute" + (time.minute == 1 ? " " : "s ")) +
                                (time.second == 0 ? "" : time.second + " second" + (time.second == 1 ? " " : "s ")) +
                                ChatColor.GREEN + " playTime!");
                    }
                    else
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
                    if(isPlayerValid(args[1])) {
                        if (Protect.protectedPlayers.contains(sender.getName())) {
                            sender.sendMessage(PlMessages.Protect_PlayerIsNoLongerProtected.get().replace("%PLAYER_NAME%", args[1]));
                            Protect.protectedPlayers.remove(args[1]);
                        } else {
                            sender.sendMessage(PlMessages.Protect_PlayerIsNowProtected.get().replace("%PLAYER_NAME%", args[1]));
                            Protect.protectedPlayers.add(args[1]);
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
                    if (Vanish.vanishedPlayers.contains(((Player)sender).getUniqueId())) {
                        sender.sendMessage(PlMessages.Vanish_YouAreNoLongerVanish.get());
                        Vanish.vanishPlayer(Bukkit.getPlayer(sender.getName()));
                    } else{
                        sender.sendMessage(PlMessages.Vanish_YouAreNowVanish.get());
                        Vanish.vanishPlayer(Bukkit.getPlayer(sender.getName()));
                    }
                } else if(args.length == 2){
                    if(isPlayerValid(args[1])) {
                        if (Vanish.vanishedPlayers.contains(Bukkit.getPlayer(args[1]).getUniqueId())) {
                            sender.sendMessage(PlMessages.Vanish_PlayerIsNoLongerVanish.get().replace("%PLAYER_NAME%", args[1]));
                            Vanish.vanishPlayer(Bukkit.getPlayer(args[1]));
                        } else {
                            sender.sendMessage(PlMessages.Vanish_PlayerIsNowVanish.get().replace("%PLAYER_NAME%", args[1]));
                            Vanish.vanishPlayer(Bukkit.getPlayer(args[1]));
                        }
                    }
                }
                break;
            case "toggle":
                if (!PlPerms.hasPerm(sender, PlPerms.Perms.ToggleCommand.get())) {
                    sender.sendMessage(PlMessages.NoPermission.get());
                    return false;
                }
                if(args.length == 3){
                    if(isPlayerValid(args[1])){
                        String item = "";
                        switch(args[2].toLowerCase()){
                            case "cps": Toggle.toggledItem.put(sender.getName(), Toggle.ToggleItems.CPS); item = "CPS"; break;
                            default:
                                sender.sendMessage(PlMessages.Toggle_TargetItemNotFind.get());
                                return false;
                        }
                        Toggle.toggledPlayers.put(sender.getName(), Bukkit.getPlayer(args[1]).getName());
                        sender.sendMessage(PlMessages.Toggle_SimplifySet.get(new RepItem("%TARGET_PLAYER%", args[1]), new RepItem("%TARGET_ITEM%", item)));
                    }
                }
        }

        return true;
    }

    public boolean isPlayerValid(String username){
        return Bukkit.getPlayer(username) != null;
    }
    public Player getPlayer(String username){
        return Bukkit.getPlayer(username);
    }
}
