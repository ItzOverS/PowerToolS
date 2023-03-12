package me.overlight.powertools.spigot.AddOns.World;

import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.Plugin.PlMessages;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemDisabler
        extends AddOn
        implements Listener {
    public ItemDisabler() {
        super("WorldAddOns.ItemDisabler", "1.0", "Prevent players from collecting some type of items", PowerTools.config.getBoolean("WorldAddOns.ItemDisabler.enabled"));
    }

    @EventHandler
    public void event(PlayerPickupItemEvent e) {
        if (PowerTools.config.getStringList(this.getName() + ".items").contains(e.getItem().getType().name())) {
            e.getPlayer().sendMessage(PlMessages.ItemDisabler_ThisItemHasBeenDisabled.get());
            e.getPlayer().getInventory().remove(e.getItem().getItemStack());
        }
    }

    @Override
    public void onEnabled() {
        new BukkitRunnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (ItemStack stack : player.getInventory().getContents()) {
                        if (stack == null) continue;
                        if (PowerTools.config.getStringList("WorldAddOns.ItemDisabler.items").contains(stack.getType().name())) {
                            player.sendMessage(PlMessages.ItemDisabler_ThisItemHasBeenDisabled.get());
                            player.getInventory().remove(stack);
                        }
                    }
                }
            }
        }.runTaskTimer(PowerTools.INSTANCE, 20, 2);
    }
}
