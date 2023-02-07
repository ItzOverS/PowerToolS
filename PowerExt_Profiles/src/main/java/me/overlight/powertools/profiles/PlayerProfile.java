package me.overlight.powertools.profiles;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.APIs.NetworkChecker;
import me.overlight.powertools.APIs.PremiumField;
import me.overlight.powertools.AddOns.Main.VersionCheck;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerProfile {
    private Player player;
    private String name, displayName, IPv4;
    private String country, city;
    private UUID uniqueID;
    private boolean isPremium;
    private int maxLMBCps;

    public void setMaxLMBCps(int maxLMBCps) {
        this.maxLMBCps = maxLMBCps;
    }

    public void setMaxRMBCps(int maxRMBCps) {
        this.maxRMBCps = maxRMBCps;
    }

    private int maxRMBCps;

    public PlayerProfile(Player player) {
        name = player.getName();
        displayName = player.getDisplayName();
        IPv4 = NetworkChecker.getPlayerIPv4(player);
        country = NetworkChecker.getPlayerCountry(player);
        city = NetworkChecker.getPlayerCity(player);
        uniqueID = player.getUniqueId();
        isPremium = NetworkChecker.isPremium(player) == PremiumField.TRUE;
        this.player = player;
    }

    public String name() {
        return name;
    }

    public String displayName() {
        return displayName;
    }

    public String IPv4() {
        return IPv4;
    }

    public String country() {
        return country;
    }

    public String city() {
        return city;
    }

    public UUID uniqueID() {
        return uniqueID;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public List<Entity> getNearestEntities(int radians) {
        List<Entity> entities = new ArrayList<Entity>();
        for (Entity entity : player.getWorld().getEntities()) {
            if (entity.getLocation().distance(player.getLocation()) < radians) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public String getClientVersion() {
        return PacketEvents.get().getPlayerUtils().getClientVersion(player).name();
    }

    public int getClientVersionProtocol() {
        return PacketEvents.get().getPlayerUtils().getClientVersion(player).getProtocolVersion();
    }

    public String getClientBrand() {
        return VersionCheck.playersClientBrand.getOrDefault(player.getName(), "N/A");
    }
}
