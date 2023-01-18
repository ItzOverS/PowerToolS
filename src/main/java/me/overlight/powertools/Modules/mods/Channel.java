package me.overlight.powertools.Modules.mods;

import me.overlight.powertools.Modules.Module;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Channel
        extends Module
        implements Listener{
    
    public static HashMap<String, String> ChatChannel = new HashMap<>(); 
    public static HashMap<String, List<String>> Channels = new HashMap<>();

    public Channel() {
        super("Channel", "Manage players chat channels", "powertools channel {channelName}", new String[] {"channel"});
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        ChatChannel.put(e.getPlayer().getName(), "ALL");
    }

    @EventHandler
    public void playerLeft(PlayerQuitEvent e){
        ChatChannel.remove(e.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerChat(AsyncPlayerChatEvent e){
        if(Freeze.frozenPlayers.contains(e.getPlayer())){
            if (!Objects.equals(ChatChannel.get(e.getPlayer().getName()), "ALL")) {
                e.setCancelled(true);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if ((Channels.get(ChatChannel.get(e.getPlayer().getName())).contains("*.*")) ? player.isOp() : getPerms(player, Channels.get(ChatChannel.get(e.getPlayer().getName())))) {
                        player.sendMessage(ChatColor.RED + "<" + e.getPlayer().getName() + "> " + ChatColor.GOLD + e.getMessage());
                    }
                }
            }
        }
    }

    private boolean getPerms(Player player, List<String> permissions) {
        for (String perm : permissions) {
            if (player.hasPermission(perm))
                return true;
        }
        return false;
    }
}
