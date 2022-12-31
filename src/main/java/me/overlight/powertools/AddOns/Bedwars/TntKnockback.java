package me.overlight.powertools.AddOns.Bedwars;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TntKnockback
        extends AddOn
        implements Listener {
    public TntKnockback() {
        super("BedwarsAddOns.TntKnockback", "1.0", "control Tnt's knockback in bedwars", "NONE", PowerTools.config.getBoolean("BedwarsAddOns.TntKnockback.enabled"));
    }
    HashMap<Player, ItemStack> handItems = new HashMap<>();

    @EventHandler
    public void explosionEvent(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return;
        if(handItems.get((Player)e.getEntity()).getType() == Material.TNT){
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                e.getEntity().setVelocity(e.getEntity().getVelocity().multiply(PowerTools.config.getDouble(this.getName() + ".multiply")));
            }, 1);
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
            handItems.put(e.getPlayer(), e.getItem());
    }
}
