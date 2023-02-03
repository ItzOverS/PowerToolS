package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.Objects;

public class WorldEnvironments
        extends AddOn
        implements Listener {

    public WorldEnvironments() {
        super("WorldEnvironments", "1.0", "Control World Environments", PowerTools.config.getBoolean("WorldEnvironments.enabled"));
    }

    @EventHandler
    public void entityDamagedEntity(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        if(!(e.getDamager() instanceof Player)) return;
        if(!PowerTools.config.getBoolean(this.getName() + ".allowPvp")) e.setCancelled(true);
    }
    @EventHandler
    public void entityDamaged(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        if(!PowerTools.config.getBoolean(this.getName() + ".allowDamage")) e.setCancelled(true);
    }
    @EventHandler
    public void entityTeleport(EntityTeleportEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        if(!PowerTools.config.getBoolean(this.getName() + ".allowTp")) e.setCancelled(true);
    }
    @EventHandler
    public void entityFlight(PlayerMoveEvent e) {
        if(!e.getPlayer().isFlying()) return;
        if(!PowerTools.config.getBoolean(this.getName() + ".allowFly")) e.setCancelled(true);
    }
    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        if(!PowerTools.config.getBoolean(this.getName() + ".allowBlockBreak")) e.setCancelled(true);
    }
    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        if(!PowerTools.config.getBoolean(this.getName() + ".allowBlockPlace")) e.setCancelled(true);
    }
    @EventHandler
    public void blockPlace(PlayerInteractEvent e) {
        if(!PowerTools.config.getBoolean(this.getName() + ".allowInteract")) e.setCancelled(true);
    }
    @EventHandler
    public void entityHunger(FoodLevelChangeEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        if (!PowerTools.config.getBoolean(this.getName() + ".allowHunger")) e.setCancelled(true);
    }
    @EventHandler
    public void playerWorldChange(PlayerPortalEvent e){
        if(PowerTools.config.getBoolean(this.getName() + ".disableEnd") && Objects.equals(e.getTo().getWorld().getName(), "world_the_end")) e.setCancelled(true);
        if(PowerTools.config.getBoolean(this.getName() + ".disableNether") && Objects.equals(e.getTo().getWorld().getName(), "world_nether")) e.setCancelled(true);
    }
}
