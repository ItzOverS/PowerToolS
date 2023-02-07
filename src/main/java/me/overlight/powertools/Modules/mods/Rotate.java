package me.overlight.powertools.Modules.mods;

import me.overlight.powertools.Modules.Module;
import me.overlight.powertools.Plugin.PlMessages;
import me.overlight.powertools.Plugin.PlPerms;
import me.overlight.powertools.Plugin.PlSticks;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Random;

public class Rotate
        extends Module
        implements Listener {
    public Rotate() {
        super("Rotate", "Rotate player to detect NoRot hack", "PowerToolS Rotate {player}", new String[]{"rotate", "rot"});
    }

    public static void testRotate(Player executor, Player player) {
        Random rand = new Random();
        int yaw = rand.nextInt(360),
                pitch = rand.nextInt(180) - 90;
        player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), yaw, pitch));
        Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
            if (player.getLocation().getYaw() == yaw && player.getLocation().getPitch() == pitch) {
                executor.sendMessage(PlMessages.Rotate_SimplifyRotated.get().replace("%PLAYER_NAME%", player.getName()));
            } else {
                executor.sendMessage(PlMessages.Rotate_FailedToRotate.get().replace("%PLAYER_NAME%", player.getName()));
            }
        }, 5);
    }


    @EventHandler
    public void playerInteractEvent(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player)) return;
        if (e.getPlayer().getItemInHand() != PlSticks.RotateStick) return;
        if (PlPerms.hasPerm(e.getPlayer(), PlPerms.Perms.RotateStick.get())) {
            testRotate(e.getPlayer(), (Player) e.getRightClicked());
        } else {
            e.getPlayer().sendMessage(PlMessages.NoPermission.get());
        }
    }
}
