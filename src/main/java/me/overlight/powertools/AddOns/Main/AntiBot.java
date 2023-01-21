package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.APIs.Infinite;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.NetworkChecker;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerTools;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import sun.nio.ch.Net;

public class AntiBot
        extends AddOn
        implements Listener {
    public AntiBot() {
        super("AntiBot", "1.0", "Prevent server from bots", "NONE", PowerTools.config.getBoolean("AntiBot.enabled"));
        new BukkitRunnable(){
            @Override
            public void run() {
                joinedUsers = 0;
            }
        }.runTaskTimerAsynchronously(PowerTools.INSTANCE, 0, 20);
    }

    int joinedUsers = 0;
    boolean antiBotMode = false;
    @EventHandler
    public void event(PlayerJoinEvent e){
        if(antiBotMode){
            Bukkit.banIP(NetworkChecker.getPlayerIPv4(e.getPlayer()));
            e.getPlayer().setBanned(true);
            e.getPlayer().kickPlayer(PlInfo.KICK_PREFIX + ChatColor.RED + "\nYou temp banned from this server");
            e.setJoinMessage(null);
            return;
        }
        joinedUsers++;
        if(joinedUsers > 10){
            for(Player player: Bukkit.getOnlinePlayers()){
                if(player.isOp()) {
                    player.sendMessage(PlInfo.PREFIX + ChatColor.RED + ChatColor.BOLD + "Server on bot attack any join will deny!");
                    continue;
                }
                player.kickPlayer(PlInfo.KICK_PREFIX + ChatColor.RED + "Server on bot attack, please rejoin 5 minutes later");
            }
            antiBotMode = true;
            new BukkitRunnable(){
                @Override
                public void run() {
                    antiBotMode = false;
                }
            }.runTaskLater(PowerTools.INSTANCE, 1200);
        }
    }
}
