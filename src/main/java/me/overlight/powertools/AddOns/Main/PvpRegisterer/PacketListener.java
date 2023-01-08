package me.overlight.powertools.AddOns.Main.PvpRegisterer;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

public class PacketListener
        extends PacketListenerAbstract {
    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if(event.getPacketId() != PacketType.Play.Client.USE_ENTITY) return;
        WrappedPacketInUseEntity action = new WrappedPacketInUseEntity(event.getNMSPacket());
        if(action.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) return;

        PvpRegisterer.CpsAtAttack.put(event.getPlayer().getName(), PvpRegisterer.CpsAtAttack.getOrDefault(event.getPlayer().getName(), 0) + 1);
    }
}
