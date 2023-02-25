package me.overlight.powertools.spigot.AddOns.Bedwars;

import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class AntiTeamUp
        extends AddOn
        implements Listener {
    public AntiTeamUp() {
        super("BedwarsAddOns.AntiTeamup", "1.0", "prevent players from team up *only* in bedwars", PowerTools.config.getBoolean("BedwarsAddOns.AntiTeamup.enabled"));
    }

    @Override
    public void onEnabled() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PowerTools.INSTANCE, this::CheckTeamUps, 10, 10);
    }

    HashMap<String, Integer> VLplayers = new HashMap<>();
    HashMap<String[], Long> getLastAttack = new HashMap<>();

    private void CheckTeamUps() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getInventory().getChestplate() == null) continue;
            if (player.getInventory().getChestplate().getType() != Material.LEATHER_CHESTPLATE) continue;
            List<Player> NearestPlayers = getNearestPlayers(player, PowerTools.config.getDouble(this.getName() + ".distance"));
            for (Player nearestPlayer : NearestPlayers) {
                player.sendMessage((((LeatherArmorMeta) player.getInventory().getChestplate().getItemMeta()).getColor() == ((LeatherArmorMeta) nearestPlayer.getInventory().getChestplate().getItemMeta()).getColor()) + "");
                if (isThereAnyWall(getHeadLocation(player), getHeadLocation(nearestPlayer))) {
                    VLplayers.put(player.getName(), 0);
                    VLplayers.put(nearestPlayer.getName(), 0);
                } else {
                    if (((LeatherArmorMeta) player.getInventory().getChestplate().getItemMeta()).getColor() == ((LeatherArmorMeta) nearestPlayer.getInventory().getChestplate().getItemMeta()).getColor())
                        return;
                    if (isDamagerContains(player.getName()))
                        if (System.currentTimeMillis() - getDamagerValue(player.getName()) < 5000)
                            break;
                    if (isDamagedEntityContains(nearestPlayer.getName()))
                        if (System.currentTimeMillis() - getDamagedEntityValue(nearestPlayer.getName()) < 5000)
                            break;

                    VLplayers.put(player.getName(), VLplayers.containsKey(player.getName()) ? VLplayers.get(player.getName()) + 1 : 1);
                    VLplayers.put(nearestPlayer.getName(), VLplayers.containsKey(nearestPlayer.getName()) ? VLplayers.get(nearestPlayer.getName()) + 1 : 1);
                }
                checkVLs(PowerTools.config.getInt(this.getName() + ".maxVL"));
            }
        }
    }

    @EventHandler
    public void playerDamageByPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) && !(e.getDamager() instanceof Player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        getLastAttack.put(new String[]{e.getDamager().getName(), e.getEntity().getName()}, System.currentTimeMillis());
    }

    private boolean isThereAnyWall(Location loc1, Location loc2) {
        if (loc1.getWorld() == loc2.getWorld()) {
            double range = 0.1;
            loc1 = new Location(loc1.getWorld(), loc1.getX(), loc1.getY(), loc1.getZ());
            loc2 = new Location(loc2.getWorld(), loc2.getX(), loc2.getY(), loc2.getZ());
            double distance = loc1.distance(loc2);
            Vector p1 = loc1.toVector();
            Vector p2 = loc2.toVector();
            Vector vector = p2.clone().subtract(p1).normalize().multiply(range);
            for (double length = 0.0; length < distance; length += range) {
                if (new Location(loc1.getWorld(), p1.getX(), p1.getY(), p1.getZ()).getBlock().getType() != Material.AIR) {
                    return true;
                }
                p1.add(vector);
            }
        }
        return false;
    }

    private List<Player> getNearestPlayers(Player target, double range) {
        List<Player> players = new ArrayList<>();
        for (Player player : target.getWorld().getPlayers()) {
            if (player == target) continue;
            if (target.getLocation().distance(player.getLocation()) <= range) {
                players.add(player);
            }
        }
        return players;
    }

    private List<Player> getNearestPlayers(Location target, double range) {
        List<Player> players = new ArrayList<>();
        for (Player player : target.getWorld().getPlayers()) {
            if (target.distance(player.getLocation()) <= range) {
                players.add(player);
            }
        }
        return players;
    }

    private Location getHeadLocation(Player player) {
        return new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ());
    }

    private void checkVLs(int maxVL) {
        for (String name : VLplayers.keySet()) {
            if (VLplayers.get(name) >= maxVL) {
                if (Bukkit.getPlayer(name) == null) {
                    VLplayers.remove(name);
                    continue;
                }
                if (!Bukkit.getPlayer(name).isOnline()) {
                    VLplayers.remove(name);
                    continue;
                }
                PowerTools.kick(Bukkit.getPlayer(name), PlInfo.KICK_PREFIX + ChatColor.RED + "Do not teamUp");
                VLplayers.put(name, 0);
            }
        }
    }

    private Player getTarget(Player attacker) {
        Location loc1 = attacker.getLocation(), loc2 = attacker.getTargetBlock((Set<Material>) null, 5).getLocation();
        if (loc1.getWorld() == loc2.getWorld()) {
            double range = 0.1;
            loc1 = new Location(loc1.getWorld(), loc1.getX(), loc1.getY() + 1, loc1.getZ());
            loc2 = new Location(loc2.getWorld(), loc2.getX(), loc2.getY() + 1, loc2.getZ());
            double distance = loc1.distance(loc2);
            Vector p1 = loc1.toVector();
            Vector p2 = loc2.toVector();
            Vector vector = p2.clone().subtract(p1).normalize().multiply(range);
            for (double length = 0.0; length < distance; length += range) {
                if (getNearestPlayers(new Location(loc1.getWorld(), p1.getX(), p1.getY(), p1.getZ()), 0.2).size() > 0) {
                    return getNearestPlayers(new Location(loc1.getWorld(), p1.getX(), p1.getY(), p1.getZ()), 0.2).get(0);
                }
                p1.add(vector);
            }
        }
        return null;
    }

    private boolean isDamagerContains(String userName) {
        for (String[] key : getLastAttack.keySet()) {
            if (Objects.equals(key[0], userName))
                return true;
        }
        return false;
    }

    private boolean isDamagedEntityContains(String userName) {
        for (String[] key : getLastAttack.keySet()) {
            if (Objects.equals(key[1], userName))
                return true;
        }
        return false;
    }

    private Long getDamagerValue(String userName) {
        for (String[] key : getLastAttack.keySet()) {
            if (Objects.equals(key[0], userName))
                return getLastAttack.get(key);
        }
        return -1L;
    }

    private Long getDamagedEntityValue(String userName) {
        for (String[] key : getLastAttack.keySet()) {
            if (Objects.equals(key[1], userName))
                return getLastAttack.get(key);
        }
        return -1L;
    }
}
