package me.overlight.powertools.spigot.Modules.mods;

import me.overlight.powertools.spigot.Libraries.RepItem;
import me.overlight.powertools.spigot.Modules.Module;
import me.overlight.powertools.spigot.Plugin.PlMessages;
import me.overlight.powertools.spigot.Plugin.PlPerms;
import me.overlight.powertools.spigot.Plugin.PlSticks;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

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
    public static void testKnockbackAll(Player executor) {
        List<String> list = new ArrayList<>();
        for(Player player: Bukkit.getOnlinePlayers()) {
            if (new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 2, player.getLocation().getZ()).getBlock() != null
                    && new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 2, player.getLocation().getZ()).getBlock().getType() != Material.AIR) {
                executor.sendMessage(PlMessages.KnockBack_PlayerInBlock.get().replace("%PLAYER_NAME%", player.getName()));
                return;
            }
            Location loc = player.getLocation();
            player.setVelocity(new Vector(2, 3, 2));
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                double distance = player.getLocation().distance(loc);
                if (!(distance > 14.5 && distance < 17.5)) {
                    list.add(player.getName());
                }
                player.teleport(loc);
            }, 7);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
            String msg = "";
            for (int i = 0; i < list.size(); i++) {
                if(i == list.size() - 1){
                    msg = msg.substring(0, msg.length() - 2);
                    msg += ChatColor.DARK_GRAY + " & " + ChatColor.GOLD + list.get(i);
                    break;
                }
                msg += ChatColor.GOLD + list.get(i) + ChatColor.DARK_GRAY + ", ";
            }
            executor.sendMessage(PlMessages.KnockBack_FailedToApply.get(new RepItem("%PLAYER_NAME%", msg)));
        }, 15);
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