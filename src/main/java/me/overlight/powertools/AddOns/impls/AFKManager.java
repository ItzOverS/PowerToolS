package me.overlight.powertools.AddOns.impls;

import me.overlight.powertools.AddOns.Main.AfkCheck;
import me.overlight.powertools.Libraries.WebHooks.DiscordAPI;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static me.overlight.powertools.PowerTools.Alert;

public class AFKManager {

    private final HashMap<Player, Long> lastMovement = new HashMap<>();
    private final HashMap<Player, Boolean> previousData = new HashMap<>();
    public static HashMap<String, String> playersListName = new HashMap<>();

    public void playerJoined(Player player) {
        lastMovement.put(player, System.currentTimeMillis());
    }

    public void playerLeft(Player player) {
        lastMovement.remove(player);
    }

    public boolean toggleAFKStatus(Player player) {

        if (isAFK(player)) {
            previousData.put(player, false);
            lastMovement.put(player, System.currentTimeMillis());
            return false;
        } else {
            previousData.put(player, true);
            lastMovement.put(player, -1L);
            return true;
        }

    }

    public void playerReacted(Player player) {

        lastMovement.put(player, System.currentTimeMillis());

        checkPlayerAFKStatus(player);

    }

    public boolean isAFK(Player player) {

        if (lastMovement.containsKey(player)) {
            if (lastMovement.get(player) == -1L) {
                return true;
            } else {
                long timeElapsed = System.currentTimeMillis() - lastMovement.get(player);
                return timeElapsed >= PowerTools.config.getLong("afkCheck.maxDelayNoAction");
            }
        } else {
            lastMovement.put(player, System.currentTimeMillis());
        }

        return false;
    }

    public void checkAllPlayersAFKStatus() {

        for (Map.Entry<Player, Long> entry : lastMovement.entrySet()) {
            checkPlayerAFKStatus(entry.getKey());
        }

    }

    public void checkPlayerAFKStatus(Player player) {
        if (lastMovement.containsKey(player)) {

            boolean nowAFK = isAFK(player);

            if (previousData.containsKey(player)) {

                boolean wasAFK = previousData.get(player);

                if (wasAFK && !nowAFK) {
                    player.sendMessage("You are no longer AFK");
                    previousData.put(player, false);

                    announceToOthers(player, false);

                } else if (!wasAFK && nowAFK) {
                    if (PowerTools.config.getBoolean("afkCheck.kickOnAFK")) {
                        kick(player.getName());
                        previousData.put(player, true);
                        lastMovement.remove(player);
                    } else {
                        player.sendMessage("You are now AFK!");
                        previousData.put(player, true);
                        announceToOthers(player, true);
                    }
                }

            } else {
                previousData.put(player, nowAFK);
            }

        }
    }

    public void announceToOthers(Player target, boolean isAFK) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isAFK) {

                if (player != target)
                    if (PowerTools.config.getBoolean("afkCheck.notification.chat.enabled"))
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', PowerTools.config.getString("afkCheck.notification.chat.AFKmessage").replace("%NAME%", target.getDisplayName())));
                if (PowerTools.config.getBoolean("afkCheck.notification.tab.enabled")) {
                    playersListName.put(target.getName(), target.getDisplayName());
                    player.setPlayerListName(player.getDisplayName() + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(PowerTools.config.getString("afkCheck.notification.tab.tag"))));
                }

            } else {
                if (player != target)
                    if (PowerTools.config.getBoolean("afkCheck.notification.chat.enabled"))
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', PowerTools.config.getString("afkCheck.notification.chat.NotAFKmessage").replace("%NAME%", target.getDisplayName())));
                if (PowerTools.config.getBoolean("afkCheck.notification.tab.enabled")) {
                    player.setPlayerListName(playersListName.get(target.getName()));
                    playersListName.remove(target.getName());
                }
            }
        }
    }
    public static void kick(String name){
        Bukkit.getScheduler().runTaskAsynchronously(PowerTools.INSTANCE, new Runnable() {
            public void run() {
                if(Bukkit.getPlayer(name) != null) {
                    if(Bukkit.getPlayer(name).isOnline()) {
                        Alert(PowerTools.Target.STAFF, ChatColor.GOLD + name + ChatColor.RED + " has kicked for AFKing");
                        AfkCheck.afkManager.playerLeft(Bukkit.getPlayer(name));
                        Bukkit.getPlayer(name).kickPlayer(PlInfo.KICK_PREFIX + ChatColor.RED + "AFK");
                        if(PowerTools.config.getBoolean("afkCheck.alert-on-discord")){
                            DiscordAPI.sendEmbedOnWebhook(name + " kicked - AFK", name + " has kicked for **afking**...");
                        }
                    }
                }
            }
        });
    }
}