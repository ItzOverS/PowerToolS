package me.overlight.powertools.spigot.AddOns.Main.AntiBot;

import me.overlight.powertools.spigot.Libraries.PluginFile;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.Plugin.PlMessages;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class Verify
        implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PlMessages.OnlyPlayersCanUseCommand.get());
            return false;
        }

        if (WhiteListManager.isWhitelist(sender.getName()))
            return false;

        AntiBot.verifyingPlayers.add(sender.getName());
        MapView map = Bukkit.createMap(((Player) sender).getWorld());
        map.getRenderers().clear();
        MapGen renderer = new MapGen();
        map.addRenderer(renderer);

        ItemStack stack = new ItemStack(Material.MAP, 1);
        ((Player) sender).getInventory().setItem(4, stack);
        ((Player) sender).sendMap(map);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (new PluginFile("AntiBot\\whitelist").getYaml().getKeys(true).contains(sender.getName())) {
                        cancel();
                        return;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ((Player) sender).sendMessage(PlInfo.PREFIX + ChatColor.GOLD + "Enter in chat, what you see in picture");
            }
        }.runTaskTimer(PowerTools.INSTANCE, 0, 100);

        return true;
    }
}
