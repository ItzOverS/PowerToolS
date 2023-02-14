package me.overlight.powertools.spigot.AddOns.Server.PluginHider;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.tabcomplete.WrappedPacketInTabComplete;
import io.github.retrooper.packetevents.packetwrappers.play.out.tabcomplete.WrappedPacketOutTabComplete;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteListener
        extends PacketListenerAbstract {
    List<String> skipTabComplete = new ArrayList<>();

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() != PacketType.Play.Client.TAB_COMPLETE) return;

        WrappedPacketInTabComplete packet = new WrappedPacketInTabComplete(event.getNMSPacket());
        if (packet.getText().split(" ").length > 1)
            skipTabComplete.add(event.getPlayer().getName());
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if (event.getPacketId() != PacketType.Play.Server.TAB_COMPLETE) return;
        if (skipTabComplete.contains(event.getPlayer().getName())) {
            skipTabComplete.remove(event.getPlayer().getName());
            return;
        }

        WrappedPacketOutTabComplete packet = new WrappedPacketOutTabComplete(event.getNMSPacket());
        List<String> matches = new ArrayList<>();
        for (String m : packet.getMatches()) {

            if (!m.contains(":")) {
                matches.add(m);
            }
        }
        String[] newMatches = new String[matches.size()];
        for (int i = 0; i < matches.size(); i++) {
            newMatches[i] = matches.get(i);
        }
        packet.setMatches(newMatches);
    }
}
