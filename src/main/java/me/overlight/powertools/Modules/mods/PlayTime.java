package me.overlight.powertools.Modules.mods;

import me.overlight.powertools.Modules.Module;
import me.overlight.powertools.Modules.impls.Timer;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayTime
        extends Module
        implements Listener, Runnable{
    public PlayTime() {
        super("PlayTime", "Manage players chat channels", "/powertools playtime {username}", new String[] {"playtime", "pt"}, 0, 500);
    }
    
    public static HashMap<String, Integer> PlayTimeTaskID = new HashMap<>();
    public static HashMap<String, Timer> PlayTime = new HashMap<>();

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        PlayTimeTaskID.put(player.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(PowerTools.INSTANCE, () -> {
            if(PlayTime.containsKey(player.getName())){
                Timer playTime = PlayTime.get(player.getName());
                playTime.add(0, 0, 1);
                PlayTime.put(player.getName(), playTime);
            } else{
                PlayTime.put(player.getName(), new Timer(0, 0, 1));
            }
        }, 20, 20));
    }

    @EventHandler
    public void playerLeft(PlayerQuitEvent e){
        try {
            Bukkit.getScheduler().cancelTask(PlayTimeTaskID.get(e.getPlayer().getName()));
            PlayTimeTaskID.remove(e.getPlayer().getName());
        } catch(Exception ignored) { }
    }

    @Override
    public void run(){
        if(System.currentTimeMillis() > 0 && System.currentTimeMillis() < 10000){
            ResetPlayersPlayTime();
        }
    }

    public void ResetPlayersPlayTime(){
        PlayTime.clear();
    }
}
