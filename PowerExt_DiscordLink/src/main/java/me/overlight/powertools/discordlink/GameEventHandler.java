package me.overlight.powertools.discordlink;

import me.overlight.powertools.Plugin.PlInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Random;

public class GameEventHandler
        implements Listener {
    public HashMap<String, Location> spawnLocation = new HashMap<>();
    @EventHandler
    public void event(PlayerJoinEvent e){
        spawnLocation.put(e.getPlayer().getName(), e.getPlayer().getLocation());
        String ranNum = generateRandomNumber();
        Player p = e.getPlayer();
        p.sendMessage(PlInfo.PREFIX + ChatColor.GREEN + ChatColor.BOLD + "Use this command in verify channel in discord: $$verify " + ChatColor.GOLD + ranNum);
        PowerExt.playerCodes.put(e.getPlayer().getName(), ranNum);
    }

    @EventHandler
    public void event(PlayerMoveEvent e){
        if(PowerExt.discordIDsUser.containsKey(e.getPlayer().getName())) return;

        e.getPlayer().teleport(spawnLocation.get(e.getPlayer().getName()));
    }

    public String generateRandomNumber(){
        Random random = new Random();
        String number = "";
        for(int i = 0; i < 5; i++) number += random.nextInt(11);
        return number;
    }
}
