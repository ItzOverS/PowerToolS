package me.overlight.powertools.profiles;

import me.overlight.powertools.NMSSupport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class SpyEventHandler
        implements Listener {
    @EventHandler
    public void event(PlayerMoveEvent e) {
        if (!PowerExt.SpyPlayers.containsValue(e.getPlayer().getName())) return;
        String key = getKey(e.getPlayer().getName());
        if (Bukkit.getPlayer(key) == null) {
            PowerExt.SpyPlayers.remove(key);
            return;
        }
        Player player = Bukkit.getPlayer(getKey(e.getPlayer().getName()));
        player.teleport(e.getPlayer().getLocation());
    }

    @EventHandler
    public void event(PlayerInteractEvent e) {
        if (!PowerExt.SpyPlayers.containsValue(e.getPlayer().getName())) return;
        String key = getKey(e.getPlayer().getName());
        if (Bukkit.getPlayer(key) == null) {
            PowerExt.SpyPlayers.remove(key);
            return;
        }
        try {
            Object packet = Objects.requireNonNull(NMSSupport.getClass("PacketPlayOutAnimation")).getConstructor(Entity.class, int.class).newInstance((Entity) e.getPlayer(), 0);
            NMSSupport.sendPacket(Bukkit.getPlayer(key), packet);
        } catch (Exception ex) {

        }
    }

    @EventHandler
    public void event(InventoryOpenEvent e) {
        if (!PowerExt.SpyPlayers.containsValue(e.getPlayer().getName())) return;
        String key = getKey(e.getPlayer().getName());
        if (Bukkit.getPlayer(key) == null) {
            PowerExt.SpyPlayers.remove(key);
            return;
        }
        Bukkit.getPlayer(key).openInventory(e.getInventory());
    }

    @EventHandler
    public void event(InventoryCloseEvent e) {
        if (!PowerExt.SpyPlayers.containsValue(e.getPlayer().getName())) return;
        String key = getKey(e.getPlayer().getName());
        if (Bukkit.getPlayer(key) == null) {
            PowerExt.SpyPlayers.remove(key);
            return;
        }
        Bukkit.getPlayer(key).closeInventory();
    }

    @EventHandler
    public void event(InventoryClickEvent e) {
        if (!PowerExt.SpyPlayers.containsValue(e.getWhoClicked().getName())) return;
        String key = getKey(e.getWhoClicked().getName());
        if (Bukkit.getPlayer(key) == null) {
            PowerExt.SpyPlayers.remove(key);
            return;
        }
        Bukkit.getPlayer(key).openInventory(e.getInventory());
    }

    public String getKey(String value) {
        for (String s : PowerExt.SpyPlayers.keySet()) {
            if (PowerExt.SpyPlayers.get(s).equals(value))
                return s;
        }
        return null;
    }
}
