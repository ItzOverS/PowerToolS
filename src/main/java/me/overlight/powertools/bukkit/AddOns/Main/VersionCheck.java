package me.overlight.powertools.bukkit.AddOns.Main;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.Libraries.RepItem;
import me.overlight.powertools.bukkit.Plugin.PlInfo;
import me.overlight.powertools.bukkit.Plugin.PlMessages;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class VersionCheck
        extends AddOn
        implements PluginMessageListener, Listener {
    public static HashMap<String, String> playersClientBrand = new HashMap<>();

    public VersionCheck() {
        super("VersionCheck", "1.0", "check players version", PowerTools.config.getBoolean("VersionCheck.enabled"), PowerTools.handleChannel("MC|Brand"));
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        String client = new String(message, StandardCharsets.UTF_8).substring(1);
        client = client.equals("LMC") ? "LabyMod" : client.equals("vanilla") ? "Vanilla" : client.startsWith("LC") ? "Pvp Lounge" : client.equals("CB") ? "CheatBreaker" :
                client.equals("eyser") ? "Geyser" : client.equals("FML") ? "Forge" : client.equals("fabric") ? "Fabric" : client.startsWith("lunarclient") ? "LunarClient" :
                        client.equals("Feather Forge") ? "FeatherClient" : client;
        playersClientBrand.put(player.getName(), client);
        PowerTools.Alert(PowerTools.Target.STAFF, PlMessages.ClDetector_PlayerJoinedUsing.get(
                new RepItem("%PLAYER_NAME%", player.getName()),
                new RepItem("%VERSION%", PacketEvents.get().getPlayerUtils().getClientVersion(player).toString().replace("v_", "").replace("_", ".")),
                new RepItem("%CLIENT%", client))
        );
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerJoinEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
            if (!playersClientBrand.containsKey(e.getPlayer().getName()))
                PowerTools.kick(e.getPlayer(), PlInfo.KICK_PREFIX + ChatColor.RED + "No Client Brand Request");
        }, 100);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerQuitEvent e) {
        playersClientBrand.remove(e.getPlayer().getName());
    }
}
