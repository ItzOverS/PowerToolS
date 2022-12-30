package me.overlight.powertools.Libraries.InvGen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InvGen {
    public static Inventory generateInv(int slots, String title, HashMap<Integer, ItemStack> items){
        Inventory inv = Bukkit.createInventory(null, slots, title);
        for(int value: items.keySet()){
            inv.setItem(value, items.get(value));
        }
        return inv;
    }
    public static ItemStack generateItem(Material mat, int amount, String displayName, String[] Lore){
        ItemStack stack = new ItemStack(mat, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(Lore));
        stack.setItemMeta(meta);
        return stack;
    }
    public static Inventory fillInv(Inventory inv, ItemStack stack, boolean override){
        Inventory i =  cloneInventory(inv, inv.getType() != InventoryType.PLAYER);
        for(int m = 0; m < inv.getSize(); m++){
            if(override)
                i.setItem(m, stack);
            else
                if(i.getItem(m) == null)
                    i.setItem(m, stack);
        }
        return i;
    }
    public static Inventory cloneInventory(Inventory inventory, boolean costumeInv){
        Inventory inv;
        if(costumeInv) inv = Bukkit.createInventory(inventory.getHolder(), inventory.getType(), inventory.getTitle());
        else inv = Bukkit.createInventory(inventory.getHolder(), inventory.getSize(), inventory.getTitle());

        for(int i = 0; i < inventory.getSize(); i++){
            if(inventory.getItem(i) != null){
                inv.setItem(i, inventory.getItem(i));
            }
        }
        return inv;
    }
}
