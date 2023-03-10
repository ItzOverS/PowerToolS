package me.overlight.powertools.spigot;

import me.overlight.powertools.spigot.AddOns.Main.AntiBot.BlackListManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainEventHandler
        implements Listener {
    private static final List<String> kickedPlayers = new ArrayList<String>();
    @EventHandler
    public void event(PlayerJoinEvent e) {
        if (BlackListManager.checkPlayer(e.getPlayer(), ChatColor.RED + "You got blacklisted by PowerAB"))
            return;
        new BukkitRunnable() {
            public void run() {
                if (!new File("plugins\\PowerToolS\\JoinedPlayers.yml").exists()) {
                    try {
                        new YamlConfiguration().save(new File("plugins\\PowerToolS\\JoinedPlayers.yml"));
                    } catch (Exception ignored) {
                    }
                }
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File("plugins\\PowerToolS\\JoinedPlayers.yml"));
                yml.set(e.getPlayer().getName(), yml.getKeys(false).size() + 1);
                try {
                    yml.save(new File("plugins\\PowerToolS\\JoinedPlayers.yml"));
                } catch (Exception ignored) {
                }
            }
        }.runTaskLater(PowerTools.INSTANCE, 10);

        PowerTools.Alert(PowerTools.Target.CONSOLE, "@color_gold" + e.getPlayer().getName() + "@color_green has joined the server", false);
        PowerTools.Alert(PowerTools.Target.CONSOLE, "@color_greenThey UUID is @color_gold" + e.getPlayer().getUniqueId(), false);
    }

    @EventHandler
    public void event(PlayerQuitEvent e) {
        if (kickedPlayers.contains(e.getPlayer().getName())) {
            kickedPlayers.remove(e.getPlayer().getName());
            return;
        }
        PowerTools.Alert(PowerTools.Target.CONSOLE, "@color_gold" + e.getPlayer().getName() + "@color_red has left the server", false);
    }

    @EventHandler
    public void event(PlayerKickEvent e) {
        if (e.getReason().contains("You got blacklisted by PowerAB"))
            PowerTools.Alert(PowerTools.Target.CONSOLE, "@color_gold" + e.getPlayer().getName() + "@color_red has blacklisted by PowerAB", false);
        else
            PowerTools.Alert(PowerTools.Target.CONSOLE, "@color_gold" + e.getPlayer().getName() + "@color_red has kicked for " + e.getReason().replace("\n", ChatColor.RESET + " | "), false);
        kickedPlayers.add(e.getPlayer().getName());
    }
}
