package me.overlight.powertools.spigot.Modules.mods;

import me.overlight.powertools.spigot.Modules.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Vanish
        extends Module
        implements Listener {
    public static List<UUID> vanishedPlayers = new ArrayList<>();

    public Vanish() {
        super("Vanish", "Hide yourself from other players to see they action without they know you're there", "PowerToolS Vanish [player]", new String[]{"vanish"});
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            e.getPlayer().showPlayer(player);
        }
        for (UUID player : vanishedPlayers) {
            e.getPlayer().hidePlayer(Bukkit.getPlayer(player));
        }
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent e) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            e.getPlayer().showPlayer(player);
        }
    }

    public static void vanishPlayer(Player player) {
        if (vanishedPlayers.contains(player.getUniqueId())) {
            vanishedPlayers.remove(player.getUniqueId());
        } else {
            vanishedPlayers.add(player.getUniqueId());
        }
    }
}
