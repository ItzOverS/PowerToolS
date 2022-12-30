package me.overlight.powertools.Modules.mods;

import me.overlight.powertools.Modules.Module;
import me.overlight.powertools.Plugin.PlMessages;
import me.overlight.powertools.Plugin.PlPerms;
import me.overlight.powertools.Plugin.PlSticks;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Freeze
        extends Module
        implements Listener {
    public static List<Player> frozenPlayers = new ArrayList<>();
    public static HashMap<Player, Location> frozenLocation = new HashMap<>();
    public Freeze() {
        super("Freeze", "Freeze player for ScreenShots", "PowerTools Freeze {player}", new String[] {"fr", "freeze"});
    }

    public static void freezePlayer(Player executor, Player player){
        if(frozenPlayers.contains(player)){
            executor.sendMessage(PlMessages.Freeze_TargetIsNoLongerFrozen.get());
            frozenPlayers.remove(player);
        } else{
            executor.sendMessage(PlMessages.Freeze_TargetIsNowFrozen.get());
            frozenPlayers.add(player);
        }
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e){
        if(frozenPlayers.contains(e.getPlayer())){
            e.getPlayer().teleport(frozenLocation.get(e.getPlayer()));
        }
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractAtEntityEvent e){
        if(!(e.getRightClicked() instanceof Player)) return;
        if(e.getPlayer().getItemInHand() != PlSticks.FreezeStick) return;
        if(PlPerms.hasPerm(e.getPlayer(), PlPerms.Perms.FreezeStick.get())){
            freezePlayer(e.getPlayer(), (Player)e.getRightClicked());
        } else{
            e.getPlayer().sendMessage(PlMessages.NoPermission.get());
        }
    }
}
