package me.overlight.powertools.spigot.AddOns.Main.AntiBot;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketStatusReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import me.overlight.powertools.spigot.APIs.NetworkChecker;
import me.overlight.powertools.spigot.PowerTools;

public class PacketListener
        extends PacketListenerAbstract {
    @Override
    public void onPacketStatusReceive(PacketStatusReceiveEvent event) {
        if (event.getPacketId() != PacketType.Status.Client.PING) return;
        String ip = NetworkChecker.getPlayerIPv4(event.getSocketAddress());
        if (BlackListManager.isBlackList(ip) && !WhiteListManager.isWhitelist(ip)) event.setCancelled(true);
        if (!(PowerTools.config.getBoolean("AntiBot.PingAttack.enabled") && !PacketEvents.get().getServerUtils().isBungeeCordEnabled()))
            return;
        AntiBot.pings++;
        if (AntiBot.pings > PowerTools.config.getInt("AntiBot.PingAttack.maxPingPerSecond")) {
            BlackListManager.blackList(ip);
            event.setCancelled(true);
        }
    }
}
