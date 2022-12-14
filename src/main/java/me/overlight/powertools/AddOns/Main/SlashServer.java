package me.overlight.powertools.AddOns.Main;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class SlashServer
        extends AddOn
        implements Listener {

    public SlashServer() {
        super("SlashSever", "1.0", "use /{servername} to switch", "NONE", PowerTools.config.getBoolean("SlashServer.enabled"));
    }

    @EventHandler
    public void commandExecute(PlayerCommandPreprocessEvent e){
        PowerTools.config.getConfigurationSection("SlashServer.servers").getKeys(false).forEach(key -> {
            if(PowerTools.config.getStringList("SlashServer.servers." + key).contains(e.getMessage().substring(1))){
                e.setCancelled(true);
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(key);
                e.getPlayer().sendPluginMessage(PowerTools.INSTANCE, "BungeeCord", out.toByteArray());
            }
        });
    }
}
