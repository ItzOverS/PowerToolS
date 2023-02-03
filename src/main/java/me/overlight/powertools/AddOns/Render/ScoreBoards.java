package me.overlight.powertools.AddOns.Render;

import me.clip.placeholderapi.PlaceholderAPI;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoards
        extends AddOn
        implements Listener {
    public ScoreBoards() {
        super("RenderAddOns.ScoreBoards", "1.0", "Show costume scoreboard in your servers", PowerTools.config.getBoolean("RenderAddOns.ScoreBoards.enabled"));
    }

    public int index = 0;

    @Override
    public void onEnabled() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PowerTools.INSTANCE, () -> {
            for(Player player: Bukkit.getOnlinePlayers()) {
                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = manager.getNewScoreboard();
                Objective obj = scoreboard.registerNewObjective(PowerTools.config.getString(this.getName() + ".name"), PowerTools.config.getString(this.getName() + ".name"));
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                List<String> items = PowerTools.config.getStringList(this.getName() + ".boards." + new ArrayList<>(PowerTools.config.getConfigurationSection(this.getName() + ".boards").getKeys(true)).get(index));
                int reverseIndex = 0;
                for(int i = items.size(); i > 0; i--){
                    createScore(i, obj, ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, items.get(reverseIndex))));
                    reverseIndex++;
                }
                player.setScoreboard(scoreboard);
            }
            index++;
            if(index >= PowerTools.config.getConfigurationSection(this.getName() + ".boards").getKeys(true).size()) index = 0;
        }, 0,  PowerTools.config.getInt(this.getName() + ".switchDelay"));
    }

    public void createScore(int index, Objective obj, String message){
        Score s = obj.getScore(message);
        s.setScore(index);
    }
}
