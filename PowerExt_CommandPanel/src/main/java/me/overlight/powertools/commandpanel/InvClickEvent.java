package me.overlight.powertools.commandpanel;

import me.clip.placeholderapi.PlaceholderAPI;
import me.overlight.powertools.PowerTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvClickEvent
        implements Listener {
    @EventHandler
    public void event(InventoryClickEvent e){
        for (String key : PowerTools.config.getConfigurationSection("CommandPanel.panels").getKeys(false)) {
            if(e.getView().getTitle().equals(PlaceholderAPI.setPlaceholders((Player) e.getWhoClicked(), ChatColor.translateAlternateColorCodes('&', PowerTools.config.getString("CommandPanel.panels." + key + ".title"))))) return;
            String uiPath = "CommandPanel.panels." + key + ".";
            e.setCancelled(PowerTools.config.getBoolean(uiPath + "cancelInteract"));
            e.getWhoClicked().setOp(true);
            ((Player) e.getWhoClicked()).performCommand(PowerTools.config.getString(uiPath + "cmd"));
            e.getWhoClicked().setOp(false);
        }
    }
}
