package me.overlight.powertools.bukkit.AddOns.Bedwars;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class FireBallKnockback
        extends AddOn
        implements Listener {
    public FireBallKnockback() {
        super("BedwarsAddOns.FireballKnockback", "1.0", "control Fireball's knockback in bedwars", PowerTools.config.getBoolean("BedwarsAddOns.FireballKnockback.enabled"));
    }

    HashMap<Player, ItemStack> handItems = new HashMap<>();

    @EventHandler
    public void explosionEvent(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return;
        if (handItems.get((Player) e.getEntity()).getType() == Material.FIREBALL) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                e.getEntity().setVelocity(e.getEntity().getVelocity().multiply(PowerTools.config.getDouble(this.getName() + ".multiply")));
            }, 1);
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
            handItems.put(e.getPlayer(), e.getItem());
    }
}
