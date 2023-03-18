package me.overlight.powertools.bukkit.Modules.mods;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.bukkit.Libraries.AlertUtils;
import me.overlight.powertools.bukkit.Modules.Module;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class Toggle
        extends Module
        implements Listener {
    public Toggle() {
        super("Toggle", "Toggle something of player for your self", "PowerToolS Toggle {player} {type}", new String[]{"toggle"});
        onEnable();
    }

    public static HashMap<String, String> toggledPlayers = new HashMap<>();
    public static HashMap<String, ToggleItems> toggledItem = new HashMap<>();

    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PowerTools.INSTANCE, () -> {
            for (String userName : toggledPlayers.keySet()) {
                Player player = Bukkit.getPlayer(userName);
                Player target = null;
                if (!toggledPlayers.get(userName).equals("Server")) {
                    target = Bukkit.getPlayer(toggledPlayers.get(userName));
                    if (target == null) {
                        toggledPlayers.remove(userName);
                        toggledItem.remove(userName);
                        continue;
                    }
                }
                if (player == null) {
                    toggledPlayers.remove(userName);
                    toggledItem.remove(userName);
                    continue;
                }
                try {
                    switch (toggledItem.get(userName)) {
                        case CPS:
                            AlertUtils.sendActionBar(player, "" + ChatColor.RED + CpsMap.LMB.getOrDefault(target.getName(), 0) + " LMB " + ChatColor.DARK_GRAY + "|" + ChatColor.RED + " RMB " + CpsMap.RMB.getOrDefault(target.getName(), 0));
                            break;
                        case TPS:
                            AlertUtils.sendActionBar(player, "" + ChatColor.RED + " TPS " + ChatColor.DARK_GRAY + " | " + ChatColor.RED + PacketEvents.get().getServerUtils().getTPS());
                            break;
                        case PING:
                            AlertUtils.sendActionBar(player, "" + ChatColor.RED + " PING " + ChatColor.DARK_GRAY + " | " + ChatColor.RED + PacketEvents.get().getPlayerUtils().getPing(target));
                            break;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 2, 2);
    }

    public enum ToggleItems {
        CPS,
        TPS,
        PING
    }
}
