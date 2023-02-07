package me.overlight.powertools.AddOns.Survival;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.Plugin.PlInfo;
import org.bukkit.*;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NoRespawn
        extends AddOn
        implements Listener {
    public NoRespawn() {
        super("SurvivalAddOns.NoRespawn", "1.0", "When player kill by another player a head will drop from dead! If that head place player will respawn... else they're spectator", false);
    }

    @EventHandler
    public void playerKillByPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player))
            return;

        if (((Player) e.getEntity()).getHealth() - e.getDamage() <= 0) {
            ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) stack.getItemMeta();
            meta.setOwner(e.getEntity().getName());
            meta.setDisplayName(ChatColor.RED + e.getEntity().getName() + "'s Head");
            stack.setItemMeta(meta);
            e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), stack);
            ((Player) e.getEntity()).setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void playerPlaceBlock(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType() == Material.SKULL) {
            Skull skull = (Skull) e.getBlockPlaced().getState();
            if (checkPlayerHead(skull) != null) {
                try {
                    e.getPlayer().sendMessage("TRUE");
                    Player player = Bukkit.getPlayer(checkPlayerHead(skull));
                    if (player == null)
                        throw new Exception("N");
                    if (!player.isOnline())
                        throw new Exception("N");
                    player.teleport(new Location(e.getBlockPlaced().getLocation().getWorld(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY() + 2, e.getPlayer().getLocation().getZ()));
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(PlInfo.PREFIX + PlInfo.ADDONS.SurvivalPrefix + ChatColor.RED + "You has respawned by " + e.getPlayer().getName());
                    e.setCancelled(false);
                } catch (Exception ex) {
                    e.getPlayer().sendMessage(PlInfo.PREFIX + PlInfo.ADDONS.SurvivalPrefix + ChatColor.RED + "Not online to respawn");
                    e.setCancelled(true);
                }
            }
        }
    }

    private boolean searchDown(List<String> list, String string) {
        for (String item : list) {
            if (Objects.equals(item, string))
                return true;
        }
        return false;
    }

    public String checkPlayerHead(Skull skull) {
        for (String str : SetToList(YamlConfiguration.loadConfiguration(new File("plugins\\PowerTools\\JoinedPlayers.yml")).getKeys(true))) {
            if (skull.getOwner().equals(str)) {
                return str;
            }
        }
        return null;
    }

    private List<String> SetToList(Set<String> set) {
        return new ArrayList<>(set);
    }
}
