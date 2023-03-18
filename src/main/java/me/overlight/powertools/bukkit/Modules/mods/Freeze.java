package me.overlight.powertools.bukkit.Modules.mods;

import me.overlight.powertools.bukkit.Modules.Module;
import me.overlight.powertools.bukkit.Plugin.PlMessages;
import me.overlight.powertools.bukkit.Plugin.PlPerms;
import me.overlight.powertools.bukkit.Plugin.PlSticks;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Freeze
        extends Module
        implements Listener {
    public static List<String> frozenPlayers = new ArrayList<>();
    public static HashMap<String, Location> frozenLocation = new HashMap<>();
    public static HashMap<String, String> playerFrozenBy = new HashMap<>();

    public Freeze() {
        super("Freeze", "Freeze player for ScreenShots", "PowerTools Freeze {player}", new String[]{"fr", "freeze"});
    }

    public static void freezePlayer(Player executor, Player player) {
        if (frozenPlayers.contains(player.getName())) {
            executor.sendMessage(PlMessages.Freeze_TargetIsNoLongerFrozen.get().replace("%PLAYER_NAME%", player.getName()));
            playerFrozenBy.remove(player.getName());
            frozenPlayers.remove(player.getName());
        } else {
            executor.sendMessage(PlMessages.Freeze_TargetIsNowFrozen.get().replace("%PLAYER_NAME%", player.getName()));
            playerFrozenBy.put(player.getName(), executor.getName());
            frozenPlayers.add(player.getName());
            frozenLocation.put(player.getName(), player.getLocation());
        }
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        if (frozenPlayers.contains(e.getPlayer().getName())) {
            e.getPlayer().teleport(frozenLocation.get(e.getPlayer().getName()));
        }
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractAtEntityEvent e) {
        if (frozenPlayers.contains(e.getPlayer().getName())) e.setCancelled(true);
        if (!(e.getRightClicked() instanceof Player)) return;
        if (e.getPlayer().getItemInHand() != PlSticks.FreezeStick) return;
        if (PlPerms.hasPerm(e.getPlayer(), PlPerms.Perms.FreezeStick.get())) {
            freezePlayer(e.getPlayer(), (Player) e.getRightClicked());
        } else {
            e.getPlayer().sendMessage(PlMessages.NoPermission.get());
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void playerChat(AsyncPlayerChatEvent e) {
        if (frozenPlayers.contains(e.getPlayer().getName())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("powertools.modules.freeze.chat")) {
                    player.sendMessage(ChatColor.GOLD + "FR: " + e.getPlayer().getName() + ": " + e.getMessage());
                }
            }
            e.getPlayer().sendMessage(ChatColor.GOLD + "You: " + e.getMessage());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerLeft(PlayerQuitEvent e) {
        if (frozenPlayers.contains(e.getPlayer().getName())) {
            Player player = Bukkit.getPlayer(playerFrozenBy.get(e.getPlayer().getName()));
            if (player == null) return;
            if (!player.isOnline()) return;

            String[] RanksSorted = {"Ignore", "Ban"};
            TextComponent[] Votes = new TextComponent[RanksSorted.length];
            TextComponent Space = new TextComponent(" ");
            BaseComponent[][] Hovers = {
                    new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&a&l&nClick to ignore that player")).create(),
                    new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&a&l&nClick to ban that player for 3 days")).create(),
            };
            HoverEvent[] HoverShows = new HoverEvent[RanksSorted.length];
            for (int i = 0; i < RanksSorted.length; i++) {
                Votes[i] = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6&l&n" + RanksSorted[i] + "&r"));
                HoverShows[i] = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Hovers[i]);
                Votes[i].setHoverEvent(HoverShows[i]);
            }
            Votes[0].setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ""));
            Votes[1].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + e.getPlayer().getName() + " 3d Refuse to SS"));
            player.sendMessage(ChatColor.RED + "Select an action");
            BaseComponent[] base = new BaseComponent[RanksSorted.length * 2];
            int index = 0;
            for (int i = 0; i < RanksSorted.length * 2; i += 2) {
                base[i] = Space;
                base[i + 1] = Votes[index];
                index++;
            }
            player.spigot().sendMessage(base);
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        if (frozenPlayers.contains(e.getPlayer().getName())) e.setCancelled(true);
    }
}
