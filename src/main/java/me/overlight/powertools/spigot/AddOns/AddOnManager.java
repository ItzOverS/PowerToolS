package me.overlight.powertools.spigot.AddOns;

import me.overlight.powertools.spigot.PowerTools;
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

    public static List<String> getName() {
        List<String> items = new ArrayList<>();
        for (AddOn addon : addOns) {
            items.add(addon.getName());
        }
        return items;
    }

    public static List<AddOn> enabledAddOns() {
        List<AddOn> addons = new ArrayList<>();
        addOns.forEach(m -> {
            if (m.isEnabled())
                addons.add(m);
        });
        return addons;
    }

    public static void registerAddOn(AddOn... addOn) {
        addOns.addAll(Arrays.asList(addOn));
    }

    public static void unRegisterAddOn(AddOn... addOn) {
        try {
            Arrays.asList(addOn).forEach(AddOn::onDisabled);
            addOns.removeAll(Arrays.asList(addOn));
        } catch (Exception ignored) {
        }
    }

    public static void unRegisterAll() {
        try {
            for (AddOn addon : addOns) {
                unRegisterAddOn(addon);
            }
        } catch (Exception ignored) {
        }
    }

    public static void loadAddons() {
        if (addOns.isEmpty()) return;
        for (AddOn addon :
                AddOnManager.addOns) {
            if (!addon.isEnabled()) {
                continue;
            }
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
