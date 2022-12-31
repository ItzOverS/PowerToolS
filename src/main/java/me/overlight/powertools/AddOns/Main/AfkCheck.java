package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.AddOns.impls.AFKManager;
import me.overlight.powertools.PowerTools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class AfkCheck
        extends AddOn
        implements Listener, Runnable {

    public AfkCheck() {
        super("AfkCheck", "1.0", "manager players afk stats", "NONE", PowerTools.config.getBoolean("AfkCheck.enabled"));
    }

    public static AFKManager afkManager = new AFKManager();
    
    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin(PlayerJoinEvent e){
        if(this.isEnabled())
            afkManager.playerJoined(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerLeft(PlayerQuitEvent e){
        if(this.isEnabled())
            afkManager.playerJoined(e.getPlayer());
    }

    @Override
    public void run() {
        afkManager.checkAllPlayersAFKStatus();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerMove(PlayerMoveEvent e){
        if(e.getFrom().distance(e.getTo()) == 0){
            if(PowerTools.config.getBoolean(this.getName() + ".Actions.ChangeHeadVictor"))
                afkManager.playerReacted(e.getPlayer());
            return;
        }
        if(PowerTools.config.getBoolean(this.getName() + ".Actions.Move"))
            afkManager.playerReacted(e.getPlayer());
    }
    @EventHandler(priority = EventPriority.LOW)
    public void playerChat(AsyncPlayerChatEvent e){
        if(PowerTools.config.getBoolean(this.getName() + ".Actions.Chat"))
            afkManager.playerReacted(e.getPlayer());
    }
    @EventHandler(priority = EventPriority.LOW)
    public void playerExecuteComamnd(PlayerCommandPreprocessEvent e){
        if(PowerTools.config.getBoolean(this.getName() + ".Actions.CommandExecute"))
            afkManager.playerReacted(e.getPlayer());
    }
    @EventHandler(priority = EventPriority.LOW)
    public void playerInteract(PlayerInteractEvent e){
        if(PowerTools.config.getBoolean(this.getName() + ".Actions.Interact"))
            afkManager.playerReacted(e.getPlayer());
    }
}
