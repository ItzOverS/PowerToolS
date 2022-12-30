package me.overlight.powertools.Modules.mods;

import me.overlight.powertools.Modules.Module;
import me.overlight.powertools.Plugin.PlMessages;
import me.overlight.powertools.Plugin.PlPerms;
import me.overlight.powertools.Plugin.PlSticks;
import me.overlight.powertools.PowerTools;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Freeze
        extends Module
        implements Listener {
    public static List<Player> frozenPlayers = new ArrayList<>();
    public static HashMap<Player, Location> frozenLocation = new HashMap<>();
    public static HashMap<Player, Player> playerFrozenBy = new HashMap<>();
    public Freeze() {
        super("Freeze", "Freeze player for ScreenShots", "PowerTools Freeze {player}", new String[] {"fr", "freeze"});
    }

    public static void freezePlayer(Player executor, Player player){
        if(frozenPlayers.contains(player)){
            executor.sendMessage(PlMessages.Freeze_TargetIsNoLongerFrozen.get());
            playerFrozenBy.remove(player);
            frozenPlayers.remove(player);
        } else{
            executor.sendMessage(PlMessages.Freeze_TargetIsNowFrozen.get());
            playerFrozenBy.put(player, executor);
            frozenPlayers.add(player);
        }
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e){
        if(frozenPlayers.contains(e.getPlayer())){
            e.getPlayer().teleport(frozenLocation.get(e.getPlayer()));
        }
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractAtEntityEvent e){
        if(!(e.getRightClicked() instanceof Player)) return;
        if(e.getPlayer().getItemInHand() != PlSticks.FreezeStick) return;
        if(PlPerms.hasPerm(e.getPlayer(), PlPerms.Perms.FreezeStick.get())){
            freezePlayer(e.getPlayer(), (Player)e.getRightClicked());
        } else{
            e.getPlayer().sendMessage(PlMessages.NoPermission.get());
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void playerChat(PlayerChatEvent e){
        if (frozenPlayers.contains(e.getPlayer())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("powertools.player.freeze.chat")) {
                    player.sendMessage(ChatColor.GOLD + "FR: " + e.getPlayer().getName() + ": " + e.getMessage());
                }
            }
            e.getPlayer().sendMessage(ChatColor.GOLD + "You: " + e.getMessage());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerLeft(PlayerQuitEvent e){
        if(frozenPlayers.contains(e.getPlayer())){
            Player player = playerFrozenBy.get(e.getPlayer());
            if(player == null) return;
            if(!player.isOnline()) return;

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
            BaseComponent[] base = new BaseComponent[RanksSorted.length*2];
            int index = 0;
            for(int i = 0; i < RanksSorted.length*2; i+=2){
                base[i] = Space;
                base[i+1] = Votes[index];
                index++;
            }
            player.spigot().sendMessage(base);
        }
    }
}
