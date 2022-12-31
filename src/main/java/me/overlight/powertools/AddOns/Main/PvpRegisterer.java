package me.overlight.powertools.AddOns.Main;

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
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PvpRegisterer
        extends AddOn
        implements Listener {
    public PvpRegisterer() {
        super("pvpRegisterer", "1.0", "register player's unregistered clicks", "NONE", PowerTools.config.getBoolean("afkCheck.enabled"));
    }
    public static HashMap<String, Integer> CpsAtAttack = new HashMap<>();
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerInteractAtEntity(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player && e.getDamager() instanceof Player))
            return;
        if(!Protect.protectedPlayers.contains(e.getEntity().getName())) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                int range = 1;
                if (CpsAtAttack.containsKey(e.getDamager().getName())) {
                    range = CpsAtAttack.get(e.getDamager().getName());
                    if (range > 10 && range < 17)
                        range = range / 2;
                    else if (range >= 17 && range < 25)
                        range = range / 3;
                    else if (range >= 25 && range < 35)
                        range = range / 4;
                    else if (range >= 35 && range < 42)
                        range = range / 5;
                    else if (range >= 42 && range < 50)
                        range = range / 6;
                }
                for (int i = 0; i < range; i++) {
                    if (PowerTools.config.getBoolean(this.getName() + ".registerDamages.enabled"))
                        ((Player) e.getEntity()).damage(PowerTools.config.getInt(this.getName() + ".registerDamages.miniDamage"));
                    if (PowerTools.config.getBoolean(this.getName() + ".registerKnockback"))
                        ((Player) e.getEntity()).setVelocity(getVectorConvert(new Vector(0.1, 0.001, 0.1), e.getDamager().getLocation().getDirection()));
                }
                CpsAtAttack.remove(e.getDamager().getName());
            }, 8);
            CpsAtAttack.put(e.getDamager().getName(), (CpsAtAttack.containsKey(e.getDamager().getName()))? CpsAtAttack.get(e.getDamager().getName()) + 1: 1);
        }
    }

    private List<Player> getNearestPlayers(double range, Player targetPlayer){
        List<Player> NearestPlayers = new ArrayList<>();
        for(Player player: targetPlayer.getWorld().getPlayers()){
            if(!targetPlayer.canSee(player))
                continue;
            if(targetPlayer.getLocation().distance(player.getLocation()) <= range)
                NearestPlayers.add(player);
        }
        return NearestPlayers;
    }

    private Vector getVectorConvert(Vector v1, Vector v2){
        double x = v1.getX(), y = v1.getY(), z = v1.getZ();

        if(String.valueOf(v2.getX()).startsWith("-") && !String.valueOf(x).startsWith("-")) x = Double.parseDouble("-" + x);
        if(!String.valueOf(v2.getX()).startsWith("-") && String.valueOf(x).startsWith("-")) x = Double.parseDouble(String.valueOf(x).substring(1));
        if(String.valueOf(v2.getY()).startsWith("-") && !String.valueOf(y).startsWith("-")) y = Double.parseDouble("-" + y);
        if(!String.valueOf(v2.getY()).startsWith("-") && String.valueOf(y).startsWith("-")) y = Double.parseDouble(String.valueOf(y).substring(1));
        if(String.valueOf(v2.getZ()).startsWith("-") && !String.valueOf(z).startsWith("-")) z = Double.parseDouble("-" + z);
        if(!String.valueOf(v2.getZ()).startsWith("-") && String.valueOf(z).startsWith("-")) z = Double.parseDouble(String.valueOf(z).substring(1));

        return new Vector(x, y, z);
    }

    public boolean canInteractAt (Player player, Location loc2){
        Location loc1 = player.getLocation();
        if(loc1.getWorld() == loc2.getWorld()){
            loc1 = new Location(loc1.getWorld(), loc1.getX(), loc1.getY() + 1, loc1.getZ());
            loc2 = new Location(loc2.getWorld(), loc2.getX(), loc2.getY() + 1, loc2.getZ());
            double distance = loc1.distance(loc2);
            Vector p1 = loc1.toVector();
            Vector p2 = loc2.toVector();
            Vector vector = p2.clone().subtract(p1).normalize().multiply(0.1);
            for (double length = 0.0; length < distance; length += 0.1) {
                if (p1.distance(loc1.toVector()) > 1.0 && p1.distance(p2) > 1.0) {
                    Location loc = new Location(player.getWorld(), p1.getX(), p1.getY(), p1.getZ());
                    if(isNearestPlayer(0.9, loc)){
                        return true;
                    }
                }
                p1.add(vector);
            }
        }
        return false;
    }

    private boolean isNearestPlayer(double range, Location location){
        for(Player player: location.getWorld().getPlayers()){
            if(location.distance(player.getLocation()) <= range)
                return true;
        }
        return false;
    }
}
