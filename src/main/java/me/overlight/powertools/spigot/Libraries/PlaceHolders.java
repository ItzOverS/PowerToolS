package me.overlight.powertools.spigot.Libraries;

import io.github.retrooper.packetevents.PacketEvents;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.overlight.powertools.spigot.AddOns.Main.VersionCheck;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceHolders
        extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "powertools";
    }

    @Override
    public String getAuthor() {
        return "_OverLight_";
    }

    @Override
    public String getVersion() {
        return PlInfo.VERSION;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (p == null) return "";
        switch (params.toLowerCase()) {
            case "cl-version":
                return PacketEvents.get().getPlayerUtils().getClientVersion(p).toString().replace("v_", "").replace("_", ".");
            case "cl-brand":
                return VersionCheck.playersClientBrand.get(p.getName());
            case "onlines-size":
                return "" + Bukkit.getOnlinePlayers().size();
            case "max-onlines-size":
                return "" + Bukkit.getServer().getMaxPlayers();
        }
        return null;
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        if (p == null) return "";
        switch (params.toLowerCase()) {
            case "onlines-size":
                return "" + Bukkit.getOnlinePlayers().size();
            case "max-onlines-size":
                return "" + Bukkit.getServer().getMaxPlayers();
        }
        return null;
    }
}
