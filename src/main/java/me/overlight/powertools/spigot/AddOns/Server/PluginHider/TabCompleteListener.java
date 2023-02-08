package me.overlight.powertools.spigot.AddOns.Server.PluginHider;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.out.tabcomplete.WrappedPacketOutTabComplete;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteListener
        extends PacketListenerAbstract {
    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if(event.getPacketId() != PacketType.Play.Server.TAB_COMPLETE) return;

        WrappedPacketOutTabComplete packet = new WrappedPacketOutTabComplete(event.getNMSPacket());
        List<String> matches = new ArrayList<>();
        for(String m: packet.getMatches()){
            if(!m.contains(":")){
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
