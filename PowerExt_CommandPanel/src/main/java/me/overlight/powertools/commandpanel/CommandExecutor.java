package me.overlight.powertools.commandpanel;

import me.overlight.powertools.Libraries.InvGen;
import me.overlight.powertools.PowerTools;
import net.minecraft.server.v1_8_R3.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class CommandExecutor
        implements org.bukkit.command.CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (String x : PowerTools.config.getConfigurationSection("CommandPanel.panels").getKeys(false)) {
            String[] cmd = PowerTools.config.getString("CommandPanel.panels." + x + ".command").split(" ");
            if(cmd.length != strings.length + 1) return false;
            if(!command.getLabel().equalsIgnoreCase(cmd[0].toLowerCase())) return false;
            if(cmd.length > 1) {
                String[] args = subString(cmd, 1, cmd.length - 1);
                boolean m = true;
                for(int i = 0; i < args.length; i++){
                    if (!Objects.equals(args[i], strings[i])) {
                        m = false;
                        break;
                    }
                }
                if(!m) return false;
                // Args & command is one!
                ((Player)commandSender).openInventory(generatePanel(x));
            }
        }
        return true;
    }

    private String[] subString(String[] list, int start, int length){
        String[] newStr = new String[length];
        System.arraycopy(list, start, newStr, 0, length);
        return newStr;
    }

    private Inventory generatePanel(String name){
        Inventory inv = InvGen.fillInv(
                Bukkit.createInventory(null, PowerTools.config.getInt("CommandPanel.panels." + name + ".rows") * 9, PowerTools.config.getString("CommandPanel.panels." + name + ".title")),
                InvGen.generateItem(Material.valueOf(PowerTools.config.getString("CommandPanel.panels." + name + ".items.fill.material")), 1, PowerTools.config.getString("CommandPanel.panels." + name + ".items.fill.name"), null),
                true
        );
        for(String InvIndex: PowerTools.config.getConfigurationSection("CommandPanel.panels." + name + ".items").getKeys(false)){
            if(InvIndex.equals("fill")) continue;
            int index = Integer.parseInt(InvIndex);
            inv.setItem(index, InvGen.generateItem(Material.valueOf(PowerTools.config.getString("panels." + name + ".panel.items." + InvIndex + ".material")), 1, PowerTools.config.getString("panels." + name + ".panel.items." + InvIndex + ".name"), null));
        }
        return inv;
    }
}
