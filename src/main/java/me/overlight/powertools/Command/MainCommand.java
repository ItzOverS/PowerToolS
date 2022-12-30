package me.overlight.powertools.Command;

import me.overlight.powertools.Libraries.ColorFormat;
import me.overlight.powertools.Libraries.InvGen.InvGen;
import me.overlight.powertools.Modules.mods.Knockback;
import me.overlight.powertools.Plugin.PlMessages;
import me.overlight.powertools.Plugin.PlPerms;
import me.overlight.powertools.Plugin.PlSticks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class MainCommand
        implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch(args[0]){
            case "knockback": case "kb":
                if(!PlPerms.hasPerm(sender, PlPerms.Perms.KnockBackCommand.get())){
                    sender.sendMessage(PlMessages.NoPermission.get());
                    return false;
                }
                if(args.length == 2){
                    if(isPlayerValid(args[1]))
                        Knockback.testKnockback((Player) sender, getPlayer(args[1]));
                    else
                        sender.sendMessage(PlMessages.PlayerNotFind.get());
                } else if(args.length == 3){
                    if(!Objects.equals(args[2], "stick")) return true;
                    if(isPlayerValid(args[1])){
                        getPlayer(args[1]).getInventory().addItem(PlSticks.KnockBackStick);
                        sender.sendMessage(PlMessages.KnockBack_StickSimplifyGiven.get().replace("%PLAYER_NAME%", getPlayer(args[1]).getName()));
                    } else{
                        sender.sendMessage(PlMessages.PlayerNotFind.get());
                    }
                }
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public boolean isPlayerValid(String username){
        return Bukkit.getPlayer(username) != null;
    }
    public Player getPlayer(String username){
        return Bukkit.getPlayer(username);
    }
}
