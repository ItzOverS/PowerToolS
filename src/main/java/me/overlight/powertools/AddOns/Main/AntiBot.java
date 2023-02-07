package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.APIs.NetworkChecker;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class AntiBot
        extends AddOn
        implements Listener {
    public AntiBot() {
        super("AntiBot", "1.0", "Prevent server from bots", PowerTools.config.getBoolean("AntiBot.enabled"));
        new BukkitRunnable() {
            @Override
            public void run() {
                joinedUsers = 0;
            }
        }.runTaskTimerAsynchronously(PowerTools.INSTANCE, 0, 20);
    }

    int joinedUsers = 0;
    boolean antiBotMode = false;
    HashMap<String, Long> userJoinTime = new HashMap<>();
    HashMap<String, List<String>> duplicateNamesGroup = new HashMap<>();

    @EventHandler
    public void event(PlayerJoinEvent e) {
        userJoinTime.put(e.getPlayer().getName(), System.currentTimeMillis());

        // -> Fast Joins
        if (PowerTools.config.getBoolean(this.getName() + ".FastJoin.enabled")) {
            if (antiBotMode) {
                Bukkit.banIP(NetworkChecker.getPlayerIPv4(e.getPlayer()));
                e.getPlayer().setBanned(true);
                e.getPlayer().kickPlayer(PlInfo.KICK_PREFIX + ChatColor.RED + "\nYou temp banned from this server");
                e.setJoinMessage(null);
                return;
            }

            joinedUsers++;
            if (joinedUsers > PowerTools.config.getInt(this.getName() + ".FastJoin.maxJoinPerSecond")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isOp()) {
                        player.sendMessage(PlInfo.PREFIX + ChatColor.RED + ChatColor.BOLD + "Server on bot attack any join will deny!");
                        continue;
                    }
                    player.kickPlayer(PlInfo.KICK_PREFIX + ChatColor.RED + "Server on bot attack, please rejoin 5 minutes later");
                }
                antiBotMode = true;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        antiBotMode = false;
                    }
                }.runTaskLater(PowerTools.INSTANCE, 1200);
            }
        }

        // -> Username Learning
        if (PowerTools.config.getBoolean(this.getName() + ".UserNameLearning.enabled")) {
            String realName = ChatManager.removeSymbols(e.getPlayer().getName(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"});
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                String p = ChatManager.removeSymbols(onlinePlayer.getName(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"});
                int startIndex = 0;
                boolean isMulti = false;
                for (char ch : realName.toCharArray()) {
                    if (p.indexOf(ch) != -1) {
                        int m = 0;
                        for (int i = startIndex; i < p.length(); i++) {
                            if (m > PowerTools.config.getInt(this.getName() + ".UserNameLearning.maxMultiLetter")) {
                                isMulti = true;
                                break;
                            }
                            if (ch == p.charAt(i)) {
                                m++;
                            } else {
                                break;
                            }
                        }
                    }
                    if (isMulti)
                        break;
                }
                if (isMulti) {
                    if (Math.max(userJoinTime.get(p), userJoinTime.get(realName)) - Math.min(userJoinTime.get(p), userJoinTime.get(realName)) < PowerTools.config.getLong(this.getName() + ".UserNameLearning.maxJoinDelay")) {
                        e.getPlayer().kickPlayer(PlInfo.KICK_PREFIX + "AntiBot: Multi Letter by name");
                    }
                }
            }
        }

        // -> MultiIP
        if (PowerTools.config.getBoolean(this.getName() + ".MultiIP.enabled")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (NetworkChecker.getPlayerIPv4(e.getPlayer()).equals(NetworkChecker.getPlayerIPv4(onlinePlayer))) {
                    Bukkit.banIP(NetworkChecker.getPlayerIPv4(e.getPlayer()));
                    onlinePlayer.setBanned(true);
                    e.getPlayer().setBanned(true);
                }
            }
        }
    }
}
