package me.overlight.powertools.spigot.AddOns.Server.PluginHider;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.out.chat.WrappedPacketOutChat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ChatMessageListener
        extends PacketListenerAbstract {
    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if (event.getPacketId() != PacketType.Play.Server.CHAT) return;
        if (event.getPlayer().isOp()) return;

        WrappedPacketOutChat packet = new WrappedPacketOutChat(event.getNMSPacket());
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            if (packet.getMessage().contains(pl.getName())) {
                event.setCancelled(true);
            }
        }
    }
}
