package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.AddOns.AddOn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.Objects;

public class JoinMessage
        extends AddOn
        implements Listener {
    public JoinMessage(boolean stats) {
        super("JoinMessage", "1.0", "show join message", "NONE", stats);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        if(this.isEnabled()) {
            String message = config.getString(this.getName() + ".messages.FirstJoin");
            if(contains("plugins\\PowerToolS\\JoinedPlayers.yml", e.getPlayer().getName())){
                message = config.getString(this.getName() + ".messages.NotFirstJoin");
            }
            assert message != null;
            message = message.replace("%NAME%", e.getPlayer().getName());
            message = message.replace("%NUM%", String.valueOf(YamlConfiguration.loadConfiguration(new File("plugins\\PowerToolS\\JoinedPlayers.yml")).getKeys(false).size()+1));
            message = message.replace("%ONLINE%", String.valueOf(Bukkit.getOnlinePlayers().size()));
            e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', message));
        }

        {
            if(!new File("plugins\\PowerToolS\\JoinedPlayers.yml").exists()){
                YamlConfiguration config = new YamlConfiguration();
                try {
                    config.save(new File("plugins\\PowerToolS\\JoinedPlayers.yml"));
                } catch(Exception ignored) { }
            }
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File("plugins\\PowerToolS\\JoinedPlayers.yml"));
            yml.set(e.getPlayer().getName(), yml.getKeys(false).size() + 1);
            try {
                yml.save(new File("plugins\\PowerToolS\\JoinedPlayers.yml"));
            } catch(Exception ignored) { }
        }
    }

    private boolean contains(String path, String text){
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File(path));
        for(String key: yml.getKeys(false)){
            if(Objects.equals(key, text))
                return true;
        }
        return false;
    }
}
