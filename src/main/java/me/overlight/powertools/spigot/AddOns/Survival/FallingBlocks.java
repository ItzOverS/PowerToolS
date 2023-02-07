package me.overlight.powertools.spigot.AddOns.Survival;

import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class FallingBlocks
        extends AddOn
        implements Listener {
    public FallingBlocks() {
        super("SurvivalAddOns.FallingBlocks", "1.0", "Calculate minecraft's gravity physics", PowerTools.config.getBoolean("SurvivalAddOns.FallingBlocks.enabled"));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (PowerTools.config.getStringList(this.getName() + ".doNotApplyOn").contains(e.getBlock().getType().name()))
            return;
        if (!PowerTools.config.getStringList(this.getName() + ".nullBlocks").contains(e.getBlock().getLocation().subtract(0, 1, 0).getBlock().getType().name()))
            return;
        new BukkitRunnable() {
            @Override
            public void run() {
                FallingBlock fBlock = e.getPlayer().getWorld().spawnFallingBlock(e.getBlock().getLocation().subtract(0, 0.05, 0), e.getBlock().getType(), e.getBlock().getData());
                e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
                fBlock.setHurtEntities(true);
                fBlock.setDropItem(false);
            }
        }.runTaskLater(PowerTools.INSTANCE, 2);
    }
}
