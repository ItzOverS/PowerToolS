package me.overlight.powertools.bukkit.Plugin;

import me.overlight.powertools.bukkit.Libraries.ColorFormat;
import me.overlight.powertools.bukkit.Libraries.InvGen;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlSticks {
    public static ItemStack KnockBackStick = InvGen.generateItem(Material.BLAZE_ROD, 1, ColorFormat.formatColor("@color_goldKnockBack check stick"), new String[]{ColorFormat.formatColor("@color_goldClick on player to test his client")});
    public static ItemStack FreezeStick = InvGen.generateItem(Material.BLAZE_ROD, 1, ColorFormat.formatColor("@color_goldFreeze stick"), new String[]{ColorFormat.formatColor("@color_goldClick on player to freeze or unfreeze that player")});
    public static ItemStack RotateStick = InvGen.generateItem(Material.BLAZE_ROD, 1, ColorFormat.formatColor("@color_goldRotate stick"), new String[]{ColorFormat.formatColor("@color_goldClick on player to rotate randomly that player")});
}
