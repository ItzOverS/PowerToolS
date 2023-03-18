package me.overlight.powertools.bukkit.AddOns.Main;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.AddOns.impls.cpsHistory;
import me.overlight.powertools.bukkit.Libraries.RepItem;
import me.overlight.powertools.bukkit.Discord.WebHooks.DiscordAPI;
import me.overlight.powertools.bukkit.Modules.mods.CpsMap;
import me.overlight.powertools.bukkit.Plugin.PlInfo;
import me.overlight.powertools.bukkit.Plugin.PlMessages;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static me.overlight.powertools.bukkit.PowerTools.Alert;

public class CpsCheck
        extends AddOn
        implements Listener {

    public static HashMap<String, Integer> AutoClickerVL = new HashMap<>();
    public static HashMap<String, cpsHistory> PlayerCpsHistory = new HashMap<>();
    public static HashMap<String, Integer> PlayerCpsTaskID = new HashMap<>();


    public CpsCheck() {
        super("CpsCheck", "1.1", "check players cps", PowerTools.config.getBoolean("CpsCheck.enabled"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        cpsHistory cpsHis = new cpsHistory();
        for (int i = 0; i < 20; i++) {
            cpsHis.addCps(cpsHistory.cpsType.LMB, 0);
            cpsHis.addCps(cpsHistory.cpsType.RMB, 0);
        }
        AutoClickerVL.put(player.getName(), 0);
        PlayerCpsHistory.put(player.getName(), cpsHis);
        checkPlayer(player);
    }

    @Override
    public void onEnabled() {
        cpsHistory cpsHis = new cpsHistory();
        for (int i = 0; i < 20; i++) {
            cpsHis.addCps(cpsHistory.cpsType.LMB, 0);
            cpsHis.addCps(cpsHistory.cpsType.RMB, 0);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            AutoClickerVL.put(player.getName(), 0);
            PlayerCpsHistory.put(player.getName(), cpsHis);
            checkPlayer(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerLeft(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        try {
            Bukkit.getScheduler().cancelTask(PlayerCpsTaskID.get(player.getName()));
        } catch (Exception ignored) {
        }
        PlayerCpsHistory.remove(player.getName());
        PlayerCpsTaskID.remove(player.getName());
    }

    private void checkPlayer(Player player) {
        PlayerCpsTaskID.put(player.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(PowerTools.INSTANCE, () -> {
            if (!player.isOnline()) {
                Bukkit.getScheduler().cancelTask(PlayerCpsTaskID.get(player.getName()));
                PlayerCpsHistory.remove(player.getName());
                PlayerCpsTaskID.remove(player.getName());
            }
            int lmb = CpsMap.LMB.getOrDefault(player.getName(), 0),
                    rmb = CpsMap.RMB.getOrDefault(player.getName(), 0);
            if (!PlayerCpsHistory.containsKey(player.getName())) {
                PlayerCpsHistory.put(player.getName(), new cpsHistory());
            }
            cpsHistory history = PlayerCpsHistory.get(player.getName());
            {
                history.addCps(cpsHistory.cpsType.LMB, lmb);
                history.addCps(cpsHistory.cpsType.RMB, rmb);
                PlayerCpsHistory.put(player.getName(), history);
            }
            if (PowerTools.config.getBoolean(this.getName() + ".maxCps.LMB.enabled")) {
                if (lmb > PowerTools.config.getLong(this.getName() + ".maxCps.LMB.max")) {
                    Alert(PowerTools.Target.STAFF, PlMessages.CpsCheck_KickedForMaxCps.get(new RepItem("%TYPE%", "lmb"), new RepItem("%PLAYER_NAME%", player.getName()), new RepItem("%CPS%", lmb + "")));
                    Bukkit.getScheduler().cancelTask(PlayerCpsTaskID.get(player.getName()));
                    for (int i = 0; i < 5; i++)
                        player.getWorld().spawnEntity(player.getLocation(), EntityType.LIGHTNING);
                    if (PowerTools.config.getBoolean(this.getName() + ".maxCps.LMB.alert-on-discord")) {
                        DiscordAPI.sendEmbedOnWebhook(player.getName() + " kicked - Max cps", "I kicked " + player.getName() + " for **MaxCPS**");
                    }
                    PowerTools.kick(player, PlInfo.KICK_PREFIX + ChatColor.RED + "\nYou has kicked for max cps");
                }
            }
            if (PowerTools.config.getBoolean(this.getName() + ".maxCps.RMB.enabled")) {
                if (rmb > PowerTools.config.getLong(this.getName() + ".maxCps.RMB.max")) {
                    Alert(PowerTools.Target.STAFF, PlMessages.CpsCheck_KickedForMaxCps.get(new RepItem("%TYPE%", "rmb"), new RepItem("%PLAYER_NAME%", player.getName()), new RepItem("%CPS%", rmb + "")));
                    Bukkit.getScheduler().cancelTask(PlayerCpsTaskID.get(player.getName()));
                    PlayerCpsHistory.remove(player.getName());
                    PlayerCpsTaskID.remove(player.getName());
                    for (int i = 0; i < 5; i++)
                        player.getWorld().spawnEntity(player.getLocation(), EntityType.LIGHTNING);
                    if (PowerTools.config.getBoolean(this.getName() + ".maxCps.RMB.alert-on-discord")) {
                        DiscordAPI.sendEmbedOnWebhook(player.getName() + " kicked - Max cps", "I kicked " + player.getName() + " for **MaxCPS**");
                    }
                    PowerTools.kick(player, PlInfo.KICK_PREFIX + ChatColor.RED + "\nYou has kicked for max cps");
                }
            }
            if (PowerTools.config.getBoolean(this.getName() + ".AutoClickerCheck.enabled")) {
                if (operator(Arrays.asList(history.getHistoryIndex(cpsHistory.cpsType.RMB, 17), history.getHistoryIndex(cpsHistory.cpsType.RMB, 18), history.getHistoryIndex(cpsHistory.cpsType.RMB, 19)), PowerTools.config.getInt(this.getName() + ".AutoClickerCheck.startCheck.RMB")) || operator(Arrays.asList(history.getHistoryIndex(cpsHistory.cpsType.LMB, 17), history.getHistoryIndex(cpsHistory.cpsType.LMB, 18), history.getHistoryIndex(cpsHistory.cpsType.LMB, 19)), PowerTools.config.getInt(this.getName() + ".AutoClickerCheck.startCheck.LMB"))) {
                    if (checkNums(DifferencesRMB(history), PowerTools.config.getInt(this.getName() + ".AutoClickerCheck.clickerRange"), DifferencesRMB(history).size() / 5) || checkNums(DifferencesLMB(history), PowerTools.config.getInt(this.getName() + ".AutoClickerCheck.clickerRange"), DifferencesLMB(history).size() / 5)) {
                        int maxVL = 5;
                        if (AutoClickerVL.get(player.getName()) >= maxVL) {
                            Alert(PowerTools.Target.STAFF, PlMessages.CpsCheck_KickedForAutoClicker.get(new RepItem("%PLAYER_NAME%", player.getName())));
                            for (int i = 0; i < 5; i++)
                                player.getWorld().spawnEntity(player.getLocation(), EntityType.LIGHTNING);
                            if (PowerTools.config.getBoolean(this.getName() + ".AutoClickerCheck.alert-on-discord")) {
                                DiscordAPI.sendEmbedOnWebhook(player.getName() + " kicked - Using AutoClicker", "I think " + player.getName() + " using **auto clicker**");
                            }
                            PowerTools.kick(player, PlInfo.KICK_PREFIX + ChatColor.RED + "\nI think you're using AutoClicker");
                            PlayerCpsHistory.remove(player.getName());
                            PlayerCpsTaskID.remove(player.getName());
                        } else {
                            Alert(PowerTools.Target.STAFF, PlMessages.CpsCheck_UsingAutoClicker.get(new RepItem("%PLAYER_NAME%", player.getName())));
                            AutoClickerVL.put(player.getName(), AutoClickerVL.get(player.getName()) + 1);
                            cpsHistory cpsHis = new cpsHistory();
                            for (int i = 0; i < 20; i++) {
                                cpsHis.addCps(cpsHistory.cpsType.LMB, 0);
                                cpsHis.addCps(cpsHistory.cpsType.RMB, 0);
                            }
                            PlayerCpsHistory.put(player.getName(), cpsHis);
                        }
                    }
                }
            }
        }, 0, 15));
    }

    private static float difference(float num1, float num2) {
        float num = num1 - num2;
        return Float.parseFloat(String.valueOf(num).replace("-", ""));
    }

    private static boolean checkNums(List<Integer> list, int maxRanges, int minVL) {
        int counter = 0;
        for (Integer num : list)
            if (num > maxRanges)
                counter++;
        return counter < minVL;
    }

    private static boolean operator(List<Integer> list, int maxRanges) {
        for (int num : list) {
            if (num <= maxRanges)
                return false;
        }
        return true;
    }

    private static List<Integer> DifferencesLMB(cpsHistory history) {
        List<Integer> diff = new ArrayList<>();
        for (int i = 19; i > 0; i--)
            diff.add((int) difference(history.getHistoryIndex(cpsHistory.cpsType.LMB, i), history.getHistoryIndex(cpsHistory.cpsType.LMB, i - 1)));
        return diff;
    }

    private static List<Integer> DifferencesRMB(cpsHistory history) {
        List<Integer> diff = new ArrayList<>();
        for (int i = 19; i > 0; i--)
            diff.add((int) difference(history.getHistoryIndex(cpsHistory.cpsType.RMB, i), history.getHistoryIndex(cpsHistory.cpsType.RMB, i - 1)));
        return diff;
    }
}
