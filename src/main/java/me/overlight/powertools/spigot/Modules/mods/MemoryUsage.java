package me.overlight.powertools.spigot.Modules.mods;

import me.overlight.powertools.spigot.Modules.Module;

import static java.lang.Runtime.getRuntime;

public class MemoryUsage
        extends Module
        implements Runnable {
    public MemoryUsage() {
        super("Memory Usage", "Check for memory usage", "", new String[]{}, 0, 100);
    }

    public static long lastMaxMemoryNotify;

    @Override
    public void run() {
        long usageMemory = (getRuntime().totalMemory() - getRuntime().freeMemory()) / 1000000;
        long totalMemory = (getRuntime().totalMemory()) / 1000000;
        if ((usageMemory / totalMemory) * 100 > 90) {
            if (System.currentTimeMillis() - lastMaxMemoryNotify > 120000) {
                lastMaxMemoryNotify = System.currentTimeMillis();
//
//                PowerTools.Alert(PowerTools.Target.STAFF, ChatColor.RED + "Server memory getting out of range: +90%");
//                PowerTools.sendEmbedOnWebhook("**ALERT**: Server memory getting out of range", "Server using +90% of total memory have!");
            }
        }
    }
}
