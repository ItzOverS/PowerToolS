package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.AddOns.AddOn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class WorldEnvironments
        extends AddOn
        implements Listener {

    public WorldEnvironments(boolean stats) {
        super("WorldEnvironments", "1.0", "Control World Environments", "NONE", stats);
    }

    @EventHandler
    public void entityDamagedEntity(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        if(!(e.getDamager() instanceof Player)) return;
        if(!config.getBoolean(this.getName() + ".allowPvp")) e.setCancelled(true);
    }
    @EventHandler
    public void entityDamaged(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        if(!config.getBoolean(this.getName() + ".allowDamage")) e.setCancelled(true);
    }
    @EventHandler
    public void entityTeleport(EntityTeleportEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        if(!config.getBoolean(this.getName() + ".allowTp")) e.setCancelled(true);
    }
    @EventHandler
    public void entityFlight(PlayerMoveEvent e) {
        if(!e.getPlayer().isFlying()) return;
        if(!config.getBoolean(this.getName() + ".allowFly")) e.setCancelled(true);
    }
    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        if(!config.getBoolean(this.getName() + ".allowBlockBreak")) e.setCancelled(true);
    }
    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        if(!config.getBoolean(this.getName() + ".allowBlockPlace")) e.setCancelled(true);
    }
    @EventHandler
    public void blockPlace(PlayerInteractEvent e) {
        if(!config.getBoolean(this.getName() + ".allowInteract")) e.setCancelled(true);
    }
    @EventHandler
    public void entityHunger(FoodLevelChangeEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        if (!config.getBoolean(this.getName() + ".allowHunger")) e.setCancelled(true);
    }
}
