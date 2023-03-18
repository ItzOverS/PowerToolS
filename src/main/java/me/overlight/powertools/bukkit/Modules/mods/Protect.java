package me.overlight.powertools.bukkit.Modules.mods;

import me.overlight.powertools.bukkit.Modules.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class Protect
        extends Module
        implements Listener {
    public Protect() {
        super("Protect", "Protect player from damages", "PowerToolS Protect {player}", new String[]{});
    }

    public static List<String> protectedPlayers = new ArrayList<>();

    @EventHandler
    public void playerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (protectedPlayers.contains(e.getEntity().getName())) {
            e.setCancelled(true);
        }
    }
}
