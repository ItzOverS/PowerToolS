package me.overlight.powertools.AddOns.Main.PvpRegisterer;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.overlight.powertools.Modules.mods.Protect;
import me.overlight.powertools.PowerTools;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Objects;

public class AttackPacketListener
        extends PacketListenerAbstract {

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if(event.getPacketId() != PacketType.Play.Client.USE_ENTITY) return;
        WrappedPacketInUseEntity action = new WrappedPacketInUseEntity(event.getNMSPacket());
        if(Protect.protectedPlayers.contains(event.getPlayer().getName())) return;
        if(action.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) return;
        if(!(action.getEntity() instanceof LivingEntity)) return;
        event.setCancelled(true);
        if (PowerTools.config.getBoolean("pvpRegisterer.registerDamages.enabled"))
            ((LivingEntity)action.getEntity()).setHealth(((LivingEntity) action.getEntity()).getHealth() - PowerTools.config.getInt("pvpRegisterer.registerDamages.miniDamage"));
        if (PowerTools.config.getBoolean("pvpRegisterer.registerKnockback")){
            Vector vec = action.getEntity().getVelocity().multiply(event.getPlayer().getLocation().getDirection()).multiply(0.5);
            if(vec.getY() < 0) vec.setY(-vec.getY());
            ((LivingEntity)action.getEntity()).setVelocity((action.getEntity().isOnGround()?event.getPlayer().getLocation().getDirection().multiply(0.1): vec));
        }
    }


    private Vector getVectorConvert(Vector v1, Vector v2){
        double x = v1.getX(), y = v1.getY(), z = v1.getZ();

        if(String.valueOf(v2.getX()).startsWith("-") && !String.valueOf(x).startsWith("-")) x = -x;
        if(!String.valueOf(v2.getX()).startsWith("-") && String.valueOf(x).startsWith("-")) x = -x;
        if(String.valueOf(v2.getY()).startsWith("-") && !String.valueOf(y).startsWith("-")) y = -y;
        if(!String.valueOf(v2.getY()).startsWith("-") && String.valueOf(y).startsWith("-")) y = -y;
        if(String.valueOf(v2.getZ()).startsWith("-") && !String.valueOf(z).startsWith("-")) z = -z;
        if(!String.valueOf(v2.getZ()).startsWith("-") && String.valueOf(z).startsWith("-")) z = -z;

        return new Vector(x, y, z);
    }

    private Vector minusVector(Vector a, Vector b) {
        return new Vector(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }
}
