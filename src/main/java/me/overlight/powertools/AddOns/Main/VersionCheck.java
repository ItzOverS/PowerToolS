package me.overlight.powertools.AddOns.Main;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.Libraries.RepItem;
import me.overlight.powertools.Plugin.PlMessages;
import me.overlight.powertools.PowerTools;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class VersionCheck
        extends AddOn
        implements PluginMessageListener {
    public static HashMap<String, String> playersClientBrand = new HashMap<>();
    public VersionCheck() {
        super("VersionCheck", "1.0", "check players version", PowerTools.config.getBoolean("VersionCheck.enabled"), (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_12))?"mc:brand":"MC|BRAND");
    }
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        try {
            String client = new String(message, "UTF-8").substring(1);
            client = client.equals("LMC")? "LabyMod": client.equals("vanilla")? "Vanilla": client.startsWith("LC")? "Pvp Lounge": client.equals("CB")? "CheatBreaker":
                    client.equals("eyser")? "Geyser": client.equals("FML")? "Forge": client.equals("fabric")? "Fabric": client.startsWith("lunarclient")? "LunarClient":
                    client.equals("Feather Forge")? "FeatherClient": client;
            playersClientBrand.put(player.getName(), client);
            PowerTools.Alert(PowerTools.Target.STAFF, PlMessages.ClDetector_PlayerJoinedUsing.get(
                    new RepItem("%PLAYER_NAME%", player.getName()),
                    new RepItem("%VERSION%", PacketEvents.get().getPlayerUtils().getClientVersion(player).toString().replace("v_", "").replace("_", ".")),
                    new RepItem("%CLIENT%", client))
            );
        } catch (UnsupportedEncodingException e) {
            PowerTools.Alert(PowerTools.Target.STAFF, PlMessages.ClDetector_FailedToDetectClient.get(new RepItem("%PLAYER_NAME%", player.getName())));
            playersClientBrand.put(player.getName(), "INVALID");
        }
    }
}
