package me.overlight.powertools.spigot;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class MainEventHandler
        implements Listener {
    @EventHandler
    public void event(PlayerJoinEvent e)
    {
        new BukkitRunnable(){
            public void run() {
                if (!new File("plugins\\PowerToolS\\JoinedPlayers.yml").exists()) {
                    YamlConfiguration config = new YamlConfiguration();
                    try {
                        PowerTools.config.save(new File("plugins\\PowerToolS\\JoinedPlayers.yml"));
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
    }
}
