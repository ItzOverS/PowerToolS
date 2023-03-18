package me.overlight.powertools.bukkit.AddOns.Main.PvpRegisterer;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.PowerTools;

import java.util.HashMap;

public class PvpRegisterer
        extends AddOn {
    public static HashMap<String, Integer> combo = new HashMap<>();

    public PvpRegisterer() {
        super("PvpRegisterer", "1.0", "register player's unregistered clicks", PowerTools.config.getBoolean("PvpRegisterer.enabled"));
    }

    @Override
    public void onEnabled() {
        PacketEvents.get().getEventManager().registerListener(new AttackPacketListener());
    }
}
