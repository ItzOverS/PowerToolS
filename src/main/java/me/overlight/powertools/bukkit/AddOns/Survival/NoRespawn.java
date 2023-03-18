package me.overlight.powertools.bukkit.AddOns.Survival;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.Plugin.PlInfo;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NoRespawn
        extends AddOn
        implements Listener {
    public NoRespawn() {
        super("SurvivalAddOns.NoRespawn", "1.0", "When player kill by another player a head will drop from dead! If that head place player will respawn... else they're spectator", PowerTools.config.getBoolean("SurvivalAddOns.NoRespawn.enabled"));
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

    List<Player> headPlaceEventActivated = new ArrayList<>();

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getItem() == null) return;
        if (e.getItem().getType() == Material.SKULL_ITEM) {
            if (checkPlayerHead(e.getItem().getItemMeta().getDisplayName()) != null) {
                try {
                    Player player = Bukkit.getPlayer(checkPlayerHead(e.getItem().getItemMeta().getDisplayName()));
                    if (player == null)
                        throw new Exception("N");
                    if (!player.isOnline())
                        throw new Exception("N");
                    player.teleport(new Location(e.getClickedBlock().getLocation().getWorld(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY() + 2, e.getPlayer().getLocation().getZ()));
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(PlInfo.PREFIX + PlInfo.ADDONS.SurvivalPrefix + ChatColor.RED + "You has respawned by " + e.getPlayer().getName());
                    headPlaceEventActivated.add(e.getPlayer());
                } catch (Exception ex) {
                    e.getPlayer().sendMessage(PlInfo.PREFIX + PlInfo.ADDONS.SurvivalPrefix + ChatColor.RED + "Not online to respawn");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void playerBlockPlace(BlockPlaceEvent e){
        if(headPlaceEventActivated.contains(e.getPlayer())){
            headPlaceEventActivated.remove(e.getPlayer());
            e.getBlockPlaced().setType(Material.AIR);
        }
    }

    public String checkPlayerHead(String skullName) {
        for (String str : SetToList(YamlConfiguration.loadConfiguration(new File("plugins\\PowerTools\\JoinedPlayers.yml")).getKeys(true))) {
            if (skullName.substring(2).replace("'s Head", "").equals(str)) {
                return str;
            }
        }
        return null;
    }

    private List<String> SetToList(Set<String> set) {
        return new ArrayList<>(set);
    }
}
