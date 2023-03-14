package me.overlight.powertools.spigot.AddOns.Main.AntiBot;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.spigot.APIs.NetworkChecker;
import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.AddOns.Main.ChatManager;
import me.overlight.powertools.spigot.Libraries.PluginFile;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AntiBot
        extends AddOn
        implements Listener {

    public static final List<String> verifyingPlayers = new ArrayList<String>();
    public static final HashMap<String, String> playerCodes = new HashMap<String, String>();

    public AntiBot() {
        super("AntiBot", "1.0", "Prevent server from bots", PowerTools.config.getBoolean("AntiBot.enabled"));
        new BukkitRunnable() {
            @Override
            public void run() {
                joinedUsers = 0;
            }
        }.runTaskTimerAsynchronously(PowerTools.INSTANCE, 0, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                pings = 0;
            }
        }.runTaskTimerAsynchronously(PowerTools.INSTANCE, 0, 20);
    }

    @Override
    public void onEnabled() {
        try {
            BlackListManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            WhiteListManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (PowerTools.config.getBoolean(this.getName() + ".PingAttack.enabled") && PacketEvents.get().getServerUtils(). isBungeeCordEnabled())
            PowerTools.INSTANCE.getServer().getMessenger().registerIncomingPluginChannel(PowerTools.INSTANCE, "pts:ab:bungee", new ChannelListener());
        else if (PowerTools.config.getBoolean(this.getName() + ".PingAttack.enabled") && !PacketEvents.get().getServerUtils(). isBungeeCordEnabled())
            PacketEvents.get().getEventManager().registerListener(new PacketListener());
        PowerTools.INSTANCE.getServer().getPluginCommand("verify").setExecutor(new Verify());
        PowerTools.INSTANCE.getServer().getPluginCommand("verify").setTabCompleter(new TabCompleteCancel());
    }

    @Override
    public void onDisabled() {
        try {
            BlackListManager.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    int joinedUsers = 0;
    boolean antiBotMode = false;
    HashMap<String, Long> userJoinTime = new HashMap<>();
    String banPrefix = PlInfo.KICK_PREFIX + ChatColor.RED + "You got blacklisted by PowerAB";

    @EventHandler
    public void event(PlayerJoinEvent e) throws IOException {
        if (BlackListManager.isBlackList(e.getPlayer().getName()))
            return;

        for (String key : new PluginFile("AntiBot\\patterns").getYaml().getKeys(true)) {
            if (isStringEqualsDiff(key, e.getPlayer().getName())) {
                BlackListManager.blackList(e.getPlayer(), banPrefix);
                return;
            }
        }
        userJoinTime.put(e.getPlayer().getName(), System.currentTimeMillis());

        // -> Fast Joins
        if (PowerTools.config.getBoolean(this.getName() + ".FastJoin.enabled")) {
            if (antiBotMode) {
                Bukkit.banIP(NetworkChecker.getPlayerIPv4(e.getPlayer()));
                BlackListManager.blackList(e.getPlayer(), banPrefix);
                PowerTools.kick(e.getPlayer(), PlInfo.KICK_PREFIX + ChatColor.RED + "\nYou temp banned from this server");
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
                    PowerTools.kick(player, PlInfo.KICK_PREFIX + ChatColor.RED + "Server on bot attack, please rejoin 5 minutes later");
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
            String diff = null;
            String realName = ChatManager.removeSymbols(e.getPlayer().getName(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"});
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer == e.getPlayer()) continue;
                String p = ChatManager.removeSymbols(onlinePlayer.getName(), new String[]{",", "|", "!", "@", "#", "$", "%", "^", "&", "(", ")", "[", "]", "{", "}", "`", "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"});
                if (p.length() == realName.length() &&
                        p.equals(realName)) {
                    diff = getStringDiff(e.getPlayer().getName(), onlinePlayer.getName());
                    if (diff != null &&
                            isStringEqualsDiff(diff, onlinePlayer.getName())) {
                        BlackListManager.blackList(e.getPlayer(), banPrefix);
                    }
                }
            }
            if (diff != null) new PluginFile("AntiBot\\patterns").insertItem(diff, "").saveYaml();
        }

        // -> MultiIP
        if (PowerTools.config.getBoolean(this.getName() + ".MultiIP.enabled")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (NetworkChecker.getPlayerIPv4(e.getPlayer()).equals(NetworkChecker.getPlayerIPv4(onlinePlayer))) {
                    Bukkit.banIP(NetworkChecker.getPlayerIPv4(e.getPlayer()));
                    BlackListManager.blackList(onlinePlayer, banPrefix);
                    BlackListManager.blackList(e.getPlayer(), banPrefix);
                }
            }
        }
    }

    private String getStringDiff(String a, String b) {
        if (a.length() != b.length()) return null;
        String diff = "";
        for (int index = 0; index < a.length(); index++) {
            if (String.valueOf(a.charAt(index)).equalsIgnoreCase(String.valueOf(b.charAt(index))))
                diff += a.charAt(index);
            else diff += "*";
        }
        return diff;
    }

    private boolean isStringEqualsDiff(String diff, String text) {
        if (diff.length() != text.length()) return false;
        for (int i = 0; i < diff.length(); i++) {
            if (diff.charAt(i) == '*') continue;
            if (diff.charAt(i) != text.charAt(i))
                return false;
        }
        return true;
    }


    @EventHandler
    public void event(PlayerMoveEvent e) {
        if (verifyingPlayers.contains(e.getPlayer().getName()))
            e.setCancelled(true);
    }

    @EventHandler
    public void event(PlayerInteractEvent e) {
        if (verifyingPlayers.contains(e.getPlayer().getName()))
            e.setCancelled(true);
    }

    @EventHandler
    public void event(EntityDamageByEntityEvent e) {
        if (verifyingPlayers.contains(e.getDamager().getName()) ||
                verifyingPlayers.contains(e.getEntity().getName()))
            e.setCancelled(true);
    }

    @EventHandler
    public void event(AsyncPlayerChatEvent e) {
        if (verifyingPlayers.contains(e.getPlayer().getName())) {
            if (playerCodes.get(e.getPlayer().getName()).equals(e.getMessage())) {
                WhiteListManager.whitelist(e.getPlayer());
                try {
                    WhiteListManager.save();
                } catch (IOException z) {
                    throw new RuntimeException(z);
                }
            } else {
                BlackListManager.blackList(e.getPlayer(), banPrefix);
            }
        }
    }

    public static int pings = 0;
}
