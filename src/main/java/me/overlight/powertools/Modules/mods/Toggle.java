package me.overlight.powertools.Modules.mods;

import me.overlight.powertools.Modules.Module;
import me.overlight.powertools.Modules.impls.AlertUtils;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
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
    public void onEnable(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PowerTools.INSTANCE, () -> {
            for (String userName : toggledPlayers.keySet()) {
                Player player = Bukkit.getPlayer(userName),
                        target = Bukkit.getPlayer(toggledPlayers.get(userName));
                if (player == null || target == null) {
                    toggledPlayers.remove(userName);
                    toggledItem.remove(userName);
                    continue;
                }

                if (toggledItem.get(userName) == ToggleItems.CPS) {
                    try {
                        AlertUtils.sendActionBar(player, "" + ChatColor.RED + CpsMap.LMB.getOrDefault(target.getName(), 0) + " LMB " + ChatColor.DARK_GRAY + "|" + ChatColor.RED + " RMB " + CpsMap.RMB.getOrDefault(target.getName(), 0));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 2, 2);
    }

    public enum ToggleItems{
        CPS,
    }
}
