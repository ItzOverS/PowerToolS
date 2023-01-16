package me.overlight.powertools.AddOns.Main.PvpRegisterer;

import de.valuga.spigot.packet.PacketHandler;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.Modules.mods.Protect;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PvpRegisterer
        extends AddOn {
    public PvpRegisterer() {
        super("pvpRegisterer", "1.0", "register player's unregistered clicks", "NONE", PowerTools.config.getBoolean("pvpRegisterer.enabled"));
        PacketEvents.get().getEventManager().registerListener(new AttackPacketListener());
    }
}
