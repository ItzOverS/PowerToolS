package me.overlight.powertools.spigot.AddOns.Survival;

import me.overlight.powertools.spigot.APIs.Vault;
import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.Libraries.InvGen;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatGames
        extends AddOn
        implements Listener {

    public int ANS, num1, num2;
    public List<Integer> rewards = new ArrayList<>();
    public boolean isComplete = true;
    public String op = "";


    public ChatGames() {
        super("SurvivalAddOns.ChatGame", "1.0", "Create a little math game with costume rewards in chat", PowerTools.config.getBoolean("SurvivalAddOns.ChatGame.enabled"));
        runTimer();
    }

    private void runTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PowerTools.INSTANCE, () -> {
            isComplete = false;
            int operator = genRand(4) + 1;
            int num1 = 0, num2 = 0, ans = 0;
            List<Integer> rewardsIndexes = new ArrayList<>();
            String oper = null;
            switch (operator) {
                case 1:
                case 2:
                    num1 = genRand(100);
                    num2 = genRand(100);
                    break;
                case 3:
                case 4:
                    num1 = genRand(10);
                    num2 = genRand(10);
                    break;
            }
            switch (operator) {
                case 1:
                    ans = num1 + num2;
                    oper = "+";
                    break;
                case 2:
                    ans = num1 - num2;
                    oper = "-";
                    break;
                case 3:
                    ans = num1 * num2;
                    oper = "×";
                    break;
                case 4:
                    ans = num1 / num2;
                    oper = "÷";
                    break;
            }
            for (int i = 0; i < PowerTools.config.getInt(this.getName() + ".rewardsPerGame"); i++) {
                int randNum = genRand(PowerTools.config.getConfigurationSection(this.getName() + ".rewards").getKeys(false).size());
                String rewardKey = this.getName() + ".rewards." + new ArrayList<>(PowerTools.config.getConfigurationSection(this.getName() + ".rewards").getKeys(false)).get(randNum);
                while (rewardKey.equals("Money")) {
                    randNum = genRand(PowerTools.config.getConfigurationSection(this.getName() + ".rewards").getKeys(false).size());
                    rewardKey = this.getName() + ".rewards." + new ArrayList<>(PowerTools.config.getConfigurationSection(this.getName() + ".rewards").getKeys(false)).get(randNum);
                }
                rewardsIndexes.add(randNum);
            }
            String rewardsInText = "";
            for (int m : rewardsIndexes) {
                String rewardKey = this.getName() + ".rewards." + new ArrayList<>(PowerTools.config.getConfigurationSection(this.getName() + ".rewards").getKeys(false)).get(m);
                rewardsInText += ChatColor.GOLD + String.valueOf(new ArrayList<>(PowerTools.config.getConfigurationSection(this.getName() + ".rewards").getKeys(false)).get(m)) + ChatColor.RED + (new ArrayList<>(PowerTools.config.getConfigurationSection(this.getName() + ".rewards").getKeys(false)).get(m).equals("Money") ? " " : "x") + PowerTools.config.getInt(rewardKey) + (new ArrayList<>(PowerTools.config.getConfigurationSection(this.getName() + ".rewards").getKeys(false)).get(m).equals("Money") ? "$" : "") + ChatColor.BLUE + ", ";
            }
            this.ANS = ans;
            this.rewards = rewardsIndexes;
            this.num1 = num1;
            this.num2 = num2;
            this.op = oper;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.GREEN + "------------ " + ChatColor.BLUE + "PowerToolS:SurvivalAddONs" + ChatColor.GREEN + " ------------");
                player.sendMessage(ChatColor.GREEN + "Q: " + ChatColor.RED + num1 + " " + oper + " " + num2 + " = ?");
                player.sendMessage(ChatColor.GREEN + "Reward" + (rewardsIndexes.size() > 1 ? "s" : "") + " : " + rewardsInText.substring(0, rewardsInText.length() - 2));
                player.sendMessage(ChatColor.GREEN + "------------------------------------------------");
            }
            if (PowerTools.config.getInt(this.getName() + ".answerTime") != -1) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                    if (this.isComplete)
                        return;
                    this.isComplete = true;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatColor.GREEN + "------------ " + ChatColor.BLUE + "PowerToolS:SurvivalAddONs" + ChatColor.GREEN + " ------------");
                        player.sendMessage(ChatColor.GREEN + "Complete! No one answered");
                        player.sendMessage(ChatColor.GREEN + "------------------------------------------------");
                    }
                }, PowerTools.config.getInt(this.getName() + ".answerTime"));
            }
        }, PowerTools.config.getInt(this.getName() + ".delay"), PowerTools.config.getInt(this.getName() + ".delay"));
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent e) {
        if (!this.isComplete) {
            try {
                if (Integer.parseInt(e.getMessage()) == this.ANS) {
                    this.isComplete = true;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatColor.GREEN + "------------ " + ChatColor.BLUE + "PowerToolS:SurvivalAddONs" + ChatColor.GREEN + " ------------");
                        player.sendMessage(ChatColor.GREEN + "Complete! " + e.getPlayer().getName() + " win! ");
                        player.sendMessage(ChatColor.GREEN + "A: " + ChatColor.RED + this.num1 + " " + this.op + " " + this.num2 + " = " + this.ANS);
                        player.sendMessage(ChatColor.GREEN + "------------------------------------------------");
                    }
                    e.setCancelled(true);
                    for (int m : this.rewards) {
                        String rewardKey = this.getName() + ".rewards." + new ArrayList<String>(PowerTools.config.getConfigurationSection(this.getName() + ".rewards").getKeys(false)).get(m);
                        String mat = new ArrayList<>(PowerTools.config.getConfigurationSection(this.getName() + ".rewards").getKeys(false)).get(m);
                        if (!mat.equals("Money"))
                            e.getPlayer().getInventory().addItem(InvGen.generateItem(Material.valueOf(mat), PowerTools.config.getInt(rewardKey), mat, null));
                        else {
                            Vault.econ().depositPlayer(Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId()), PowerTools.config.getInt(rewardKey));
                        }
                    }
                    e.getPlayer().sendMessage(PlInfo.PREFIX + PlInfo.ADDONS.SurvivalPrefix + ChatColor.GREEN + "You recived your reward" + (this.rewards.size() > 1 ? "s" : ""));
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static int genRand(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
}
