package me.overlight.powertools.spigot.AddOns.Main.PvpRegisterer;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.overlight.powertools.spigot.Modules.mods.Protect;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class AttackPacketListener
        extends PacketListenerAbstract {

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() != PacketType.Play.Client.USE_ENTITY) return;
        WrappedPacketInUseEntity action = new WrappedPacketInUseEntity(event.getNMSPacket());
        if (Protect.protectedPlayers.contains(event.getPlayer().getName())) return;
        if (action.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) return;
        Random random = new Random();
        if (random.nextInt(100) + 1 > PowerTools.config.getInt("pvpRegisterer.chance")) return;
        PvpRegisterer.combo.put(event.getPlayer().getName(), PvpRegisterer.combo.getOrDefault(event.getPlayer().getName(), 0) + 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                PvpRegisterer.combo.put(event.getPlayer().getName(), PvpRegisterer.combo.get(event.getPlayer().getName()) - 1);
            }
        }.runTaskLater(PowerTools.INSTANCE, 20);
        if (PvpRegisterer.combo.getOrDefault(event.getPlayer().getName(), 0) < 6) return;
        if (!(action.getEntity() instanceof LivingEntity)) return;
        if (action.getEntity() instanceof Player)
            if (((Player) action.getEntity()).getGameMode() == GameMode.CREATIVE || ((Player) action.getEntity()).getGameMode() == GameMode.ADVENTURE)
                return;
        if (PowerTools.config.getBoolean("pvpRegisterer.registerDamages"))
            if (((LivingEntity) action.getEntity()).getHealth() - 1 > -1)
                ((LivingEntity) action.getEntity()).setHealth(((LivingEntity) action.getEntity()).getHealth() - 1);
        if (PowerTools.config.getBoolean("pvpRegisterer.registerKnockback")) {
            Vector vec = action.getEntity().getVelocity().multiply(event.getPlayer().getLocation().getDirection()).multiply(0.5);
            if (vec.getY() < 0) vec.setY(-vec.getY());
            action.getEntity().setVelocity((action.getEntity().isOnGround() ? event.getPlayer().getLocation().getDirection().multiply(0.3) : vec));
        }
    }


    private Vector getVectorConvert(Vector v1, Vector v2) {
        double x = v1.getX(), y = v1.getY(), z = v1.getZ();

        if (String.valueOf(v2.getX()).startsWith("-") && !String.valueOf(x).startsWith("-")) x = -x;
        if (!String.valueOf(v2.getX()).startsWith("-") && String.valueOf(x).startsWith("-")) x = -x;
        if (String.valueOf(v2.getY()).startsWith("-") && !String.valueOf(y).startsWith("-")) y = -y;
        if (!String.valueOf(v2.getY()).startsWith("-") && String.valueOf(y).startsWith("-")) y = -y;
        if (String.valueOf(v2.getZ()).startsWith("-") && !String.valueOf(z).startsWith("-")) z = -z;
        if (!String.valueOf(v2.getZ()).startsWith("-") && String.valueOf(z).startsWith("-")) z = -z;

        return new Vector(x, y, z);
    }

    private Vector minusVector(Vector a, Vector b) {
        return new Vector(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }
}
