package me.overlight.powertools.AddOns.Hub;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.AddOns.impls.VelcPlate;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KnockbackPlate
        extends AddOn
        implements Listener {
    private final List<VelcPlate> plates = new ArrayList<>();
    public KnockbackPlate() {
        super("HubAddOns.KnockbackPlates", "1.0", "Just create some knockback plates", PowerTools.config.getBoolean("HubAddOns.KnockbackPlates.enabled"));
        for(String key: PowerTools.config.getConfigurationSection(this.getName() + ".locations").getKeys(false)){
            plates.add(
                    new VelcPlate(
                            new Location(Bukkit.getWorld(PowerTools.config.getString(this.getName() + ".locations." + key + ".world")), PowerTools.config.getIntegerList(this.getName() + ".locations." + key + ".location").get(0), 0, PowerTools.config.getIntegerList(this.getName() + ".locations." + key + ".location").get(1)),
                            Objects.equals(PowerTools.config.getString(this.getName() + ".locations." + key + ".mode"), "HEAD") ? VelcPlate.Mode.Head : VelcPlate.Mode.Force,
                            PowerTools.config.getDouble(this.getName() + ".locations." + key + ".multiply"),
                            PowerTools.config.getDouble(this.getName() + ".locations." + key + ".verticalNormal"),
                            new Vector(PowerTools.config.getDoubleList(this.getName() + ".locations." + key + ".knockback").get(0), PowerTools.config.getDoubleList(this.getName() + ".locations." + key + ".knockback").get(1), PowerTools.config.getDoubleList(this.getName() + ".locations." + key + ".knockback").get(2))
                    )
            );
        }
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent e){
        for(VelcPlate plate: plates){
            if(opLoc(e.getPlayer().getLocation(), plate.getLoc())){
                if(plate.getMode() ==  VelcPlate.Mode.Force)
                    e.getPlayer().setVelocity(plate.getKnockback());
                else
                    e.getPlayer().setVelocity(new Vector(
                            e.getPlayer().getLocation().getDirection().getX() * plate.getMultiply(),
                            plate.getVerticalMultiply(),
                            e.getPlayer().getLocation().getDirection().getZ() * plate.getMultiply()
                    ));
            }
        }
    }

    private boolean opLoc (Location loc, Location targ){
        return
                loc.getBlockX() == targ.getBlockX() &&
                loc.getBlockZ() == targ.getBlockZ();
    }
}
