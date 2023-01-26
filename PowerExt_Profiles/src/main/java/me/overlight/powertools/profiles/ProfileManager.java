package me.overlight.powertools.profiles;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileManager {
    public static final HashMap<String, PlayerProfile> profiles = new HashMap<>();

    public static void addProfile(Player player){
        profiles.put(player.getName(), new PlayerProfile(player));
    }

    public static void removeProfile(Player player){
        profiles.remove(player.getName());
    }

    public static PlayerProfile getProfile(Player player){
        return profiles.get(player.getName());
    }
}
