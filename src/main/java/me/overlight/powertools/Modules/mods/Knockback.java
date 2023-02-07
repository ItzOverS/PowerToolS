package me.overlight.powertools.Modules.mods;

import me.overlight.powertools.Modules.Module;
import me.overlight.powertools.Plugin.PlMessages;
import me.overlight.powertools.Plugin.PlPerms;
import me.overlight.powertools.Plugin.PlSticks;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;

public class Knockback
        extends Module
        implements Listener {
    public Knockback() {
        super("KnockBack", "Knockback a player to detect velocity", "PowerToolS Knockback {player}", new String[]{"kb", "knockback"});
    }

    public static void testKnockback(Player executor, Player player) {
        if (new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 2, player.getLocation().getZ()).getBlock() != null
                && new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 2, player.getLocation().getZ()).getBlock().getType() != Material.AIR) {
            executor.sendMessage(PlMessages.KnockBack_PlayerInBlock.get().replace("%PLAYER_NAME%", player.getName()));
            return;
        }
        Location loc = player.getLocation();
        player.setVelocity(new Vector(2, 3, 2));
        Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
            if (player.getLocation().distance(loc) < 8) {
                executor.sendMessage(PlMessages.KnockBack_FailedToApply.get().replace("%PLAYER_NAME%", player.getName()));
            } else {
                executor.sendMessage(PlMessages.KnockBack_SimplifyApplied.get().replace("%PLAYER_NAME%", player.getName()));
            }
            player.teleport(loc);
        }, 7);
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player)) return;
        if (e.getPlayer().getItemInHand() != PlSticks.KnockBackStick) return;
        if (PlPerms.hasPerm(e.getPlayer(), PlPerms.Perms.KnockBackStick.get())) {
            testKnockback(e.getPlayer(), (Player) e.getRightClicked());
        } else {
            e.getPlayer().sendMessage(PlMessages.NoPermission.get());
        }
    }
}