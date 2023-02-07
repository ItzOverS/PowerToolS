package me.overlight.powertools.AddOns;

import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddOnManager {
    public static List<AddOn> addOns = new ArrayList<>();

    public static AddOn getAddOnByName(String name) {
        for (AddOn on : addOns) {
            if (Objects.equals(on.getName(), name)) {
                return on;
            }
        }
        return null;
    }

    public static void registerAddOn(AddOn... addOn) {
        addOns.addAll(Arrays.asList(addOn));
    }

    public static void unRegisterAddOn(AddOn... addOn) {
        try {
            addOns.removeAll(Arrays.asList(addOn));
            Arrays.asList(addOn).forEach(AddOn::onDisabled);
        } catch (Exception ignored) {
        }
    }

    public static void unRegisterAll() {
        try {

            addOns.forEach(AddOn::onDisabled);
            addOns.clear();
        } catch (Exception ignored) {
        }
    }

    public static void loadAddons() {
        for (AddOn addon : addOns) {
            if (!addon.isEnabled()) continue;
            if (addon instanceof Listener)
                PowerTools.INSTANCE.getServer().getPluginManager().registerEvents((Listener) addon, PowerTools.INSTANCE);
            if (addon instanceof Runnable)
                Bukkit.getScheduler().runTaskTimerAsynchronously(PowerTools.INSTANCE, (Runnable) addon, 0, 40);
            if (addon instanceof CommandExecutor)
                PowerTools.INSTANCE.getServer().getPluginCommand("powertools").setExecutor((CommandExecutor) addon);
            if (addon instanceof PluginMessageListener)
                if (addon.channel() != null)
                    PowerTools.INSTANCE.getServer().getMessenger().registerIncomingPluginChannel(PowerTools.INSTANCE, addon.channel(), (PluginMessageListener) addon);
        }
    }
}
