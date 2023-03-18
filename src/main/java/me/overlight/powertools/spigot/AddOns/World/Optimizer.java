package me.overlight.powertools.spigot.AddOns.World;

import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.Libraries.RepItem;
import me.overlight.powertools.spigot.Plugin.PlMessages;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class Optimizer
        extends AddOn
        implements Listener {
    public Optimizer() {
        super("WorldAddOns.Optimizer", "1.0", "Optimize world for cpu & memory usage", PowerTools.config.getBoolean("WorldAddOns.Optimizer.enabled"));
    }

    boolean isGettingClean = false;

    @Override
    public void onEnabled() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isGettingClean) return;
                for (World world : Bukkit.getWorlds()) {
                    if (getEntitiesWithRules(world).size() == 0) return;
                    isGettingClean = true;
                    PowerTools.Alert(PowerTools.Target.MEMBERS, PlMessages.Optimizer_ItemWillRemovedIn.get(new RepItem("%OBJ%", (PowerTools.config.getBoolean(getName() + ".ItemRemover") ? "Items" : "") + (PowerTools.config.getBoolean(getName() + ".ItemRemover") && PowerTools.config.getBoolean(getName() + ".ItemRemover") ? " & " : "") + (PowerTools.config.getBoolean(getName() + ".EntityRemover") ? "Entities" : "")), new RepItem("%TIME%", String.valueOf(PowerTools.config.getInt(getName() + ".remove-interval") / 20))));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            List<Entity> ents = getEntitiesWithRules(world);
                            if (PowerTools.config.getBoolean(getName() + ".ItemRemover"))
                                ents.stream().filter(ent -> ent.getType() == EntityType.DROPPED_ITEM).collect(Collectors.toList()).forEach(Entity::remove);
                            if (PowerTools.config.getBoolean(getName() + ".EntityRemover"))
                                ents.stream().filter(ent -> ent.getType() != EntityType.DROPPED_ITEM).collect(Collectors.toList()).forEach(Entity::remove);

                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (PowerTools.config.getBoolean(getName() + ".ItemRemover") && PowerTools.config.getBoolean(getName() + ".EntityRemover")) {
                                    player.sendMessage(PlMessages.Optimizer_SimplifyRemovedItemsAndMobs.get(new RepItem("%ITEMS%", (int) ents.stream().filter(ent -> ent.getType() == EntityType.DROPPED_ITEM).count() + ""), new RepItem("%MOBS%", (int) ents.stream().filter(ent -> ent.getType() != EntityType.DROPPED_ITEM).count() + "")));
                                    continue;
                                }
                                if (PowerTools.config.getBoolean(getName() + ".ItemRemover"))
                                    player.sendMessage(PlMessages.Optimizer_SimplifyRemovedItems.get(new RepItem("%ITEMS%", (int) ents.stream().filter(ent -> ent.getType() == EntityType.DROPPED_ITEM).count() + "")));
                                if (PowerTools.config.getBoolean(getName() + ".EntityRemover"))
                                    player.sendMessage(PlMessages.Optimizer_SimplifyRemovedMobs.get(new RepItem("%MOBS%", (int) ents.stream().filter(ent -> ent.getType() != EntityType.DROPPED_ITEM).count() + "")));
                            }

                            isGettingClean = false;
                        }
                    }.runTaskLater(PowerTools.INSTANCE, PowerTools.config.getInt(getName() + ".remove-interval"));

                    for (int value : PowerTools.config.getIntegerList(getName() + ".AlertInterval")) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                PowerTools.Alert(PowerTools.Target.MEMBERS, PlMessages.Optimizer_ItemWillRemovedIn.get(new RepItem("%OBJ%", (PowerTools.config.getBoolean(getName() + ".ItemRemover") ? "Items" : "") + (PowerTools.config.getBoolean(getName() + ".ItemRemover") && PowerTools.config.getBoolean(getName() + ".ItemRemover") ? " & " : "") + (PowerTools.config.getBoolean(getName() + ".EntityRemover") ? "Entities" : "")), new RepItem("%TIME%", String.valueOf((PowerTools.config.getInt(getName() + ".remove-interval") / 20) - (value / 20)))));
                            }
                        }.runTaskLater(PowerTools.INSTANCE, value);
                    }
                }
            }
        }.runTaskTimer(PowerTools.INSTANCE, PowerTools.config.getInt(this.getName() + ".check-interval"), PowerTools.config.getInt(this.getName() + ".check-interval"));
    }

    private List<Entity> getEntitiesWithRules(World world) {
        return world.getEntities().stream().filter(ent -> ent.getType() != EntityType.PLAYER && (
                (ent.getType() == EntityType.BOAT && !((Boat) ent).isInsideVehicle()) ||
                        (ent.getType() == EntityType.MINECART && !((Minecart) ent).isInsideVehicle()) ||
                        ent.getType() != EntityType.VILLAGER ||
                        ent.getCustomName() == null)
        ).collect(Collectors.toList());
    }
}
