package me.overlight.powertools.AddOns.Main;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.Libraries.RepItem;
import me.overlight.powertools.Plugin.PlMessages;
import me.overlight.powertools.PowerTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.UnsupportedEncodingException;

public class VersionCheck
        extends AddOn
        implements PluginMessageListener {
    public VersionCheck() {
        super("VersionCheck", "1.0", "check players version", "NONE", PowerTools.config.getBoolean("VersionCheck.enabled"), "MC|Brand");
    }
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        try {
            String client = new String(message, "UTF-8").substring(1);
            client = client.equals("LMC")? "LabyMod": client.equals("vanilla")? "Vanilla": client.startsWith("LC")? "Pvp Lounge": client.equals("CB")? "CheatBreaker":
                    client.equals("eyser")? "Geyser": client.equals("FML")? "Forge": client.equals("fabric")? "Fabric": client.startsWith("lunarclient")? "LunarClient":
                    client.equals("Feather Forge")? "FeatherClient": client;
            PowerTools.Alert(PowerTools.Target.STAFF, PlMessages.PlayerJoinedUsing.get(
                    new RepItem("%PLAYER_NAME%", player.getName()),
                    new RepItem("%VERSION%", PacketEvents.get().getPlayerUtils().getClientVersion(player).toString().replace("v_", "").replace("_", ".")),
                    new RepItem("%CLIENT%", client))
            );
        } catch (UnsupportedEncodingException e) {
            PowerTools.Alert(PowerTools.Target.STAFF, PlMessages.FailedToDetectClient.get(new RepItem("%PLAYER_NAME%", player.getName())));
        }
    }
}
