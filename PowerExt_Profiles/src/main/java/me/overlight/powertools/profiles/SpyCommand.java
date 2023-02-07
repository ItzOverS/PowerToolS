package me.overlight.powertools.profiles;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpyCommand
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        if (!commandSender.isOp()) return false;
        if (strings.length != 1) return false;
        if (strings[0].equals("DENY")) {
            PowerExt.SpyPlayers.remove(commandSender.getName());
            ((Player) commandSender).teleport(PowerExt.SpyStartLocation.get(commandSender.getName()));
            PowerExt.SpyStartLocation.remove(commandSender.getName());
            return true;
        }
        if (Bukkit.getPlayer(strings[0]) == null) return false;
        Player target = Bukkit.getPlayer(strings[0]);
        PowerExt.SpyStartLocation.put(commandSender.getName(), ((Player) commandSender).getLocation());
        PowerExt.SpyPlayers.put(commandSender.getName(), target.getName());
        return true;
    }
}
