package me.overlight.powertools.spigot.AddOns.Survival;

import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.File;
import java.util.Objects;
import java.util.Random;

public class RandomSpawn
        extends AddOn
        implements Listener {
    public RandomSpawn() {
        super("SurvivalAddOns.RandomSpawn", "1.0", "Random spawn player when join server for first time", PowerTools.config.getBoolean("SurvivalAddOns.RandomSpawn.enabled"));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerJoin(PlayerJoinEvent e) {
        if (!contains("plugins\\PowerToolS\\JoinedPlayers.yml", e.getPlayer().getName())) {
            Random random = new Random();
            int x = random.nextInt(5000000) - 2500000, z = random.nextInt(5000000) - 2500000, y = (e.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER)) ? getHighestNether(e.getPlayer().getWorld(), x, z) : e.getPlayer().getWorld().getHighestBlockYAt(x, z);
            e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), x, y, z), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    private boolean contains(String path, String text) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File(path));
        for (String key : yml.getKeys(false)) {
            if (Objects.equals(key, text))
                return true;
        }
        return false;
    }

    private int getHighestNether(World world, int x, int z) {
        for (int y = 0; y < 128; y++) {
            if (world.getBlockAt(x, y, z).isEmpty() && world.getBlockAt(x, y + 1, z).isEmpty()
                    && world.getBlockAt(x, y + 2, z).isEmpty())
                return y;
        }
        return -1;
    }
}
