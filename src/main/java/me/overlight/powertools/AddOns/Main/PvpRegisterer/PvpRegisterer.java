package me.overlight.powertools.AddOns.Main.PvpRegisterer;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;

import java.util.HashMap;

public class PvpRegisterer
        extends AddOn {
    public static HashMap<String, Integer> combo = new HashMap<>();

    public PvpRegisterer() {
        super("pvpRegisterer", "1.0", "register player's unregistered clicks", PowerTools.config.getBoolean("pvpRegisterer.enabled"));
    }

    @Override
    public void onEnabled() {
        PacketEvents.get().getEventManager().registerListener(new AttackPacketListener());
    }
}
