package me.overlight.powertools.Libraries;

import io.github.retrooper.packetevents.PacketEvents;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.overlight.powertools.AddOns.Main.VersionCheck;
import me.overlight.powertools.Plugin.PlInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceHolders
        extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "PowerToolS";
    }

    @Override
    public String getAuthor() {
        return "_OverLight_";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if(params.equalsIgnoreCase("cl-version")){
            return PacketEvents.get().getPlayerUtils().getClientVersion(p).toString().replace("v_", "").replace("_", ".");
        } else if(params.equalsIgnoreCase("cl-brand")){
            return VersionCheck.playersClientBrand.get(p.getName());
        } else if(params.equalsIgnoreCase("player-name")){
            return p.getName();
        } else if(params.equalsIgnoreCase("player-display")) {
            return p.getDisplayName();
        } else if(params.equalsIgnoreCase("onlines-size")){
            return "" + Bukkit.getOnlinePlayers().size();
        }
        return null;
    }
}
