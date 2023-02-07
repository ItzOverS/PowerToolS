package me.overlight.powertools.APIs;

import me.overlight.powertools.PowerTools;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Vault {
    private static Economy econ;
    private static Chat chat;
    private static Permission perms;

    public static boolean implementAPI() {
        if (VaultContains()) {
            if (!VaultEnabled())
                enabledVault();
        } else {
            return false;
        }
        return setupEconomy() && setupChat() && setupPermissions();
    }

    public static void enabledVault() {
        PowerTools.INSTANCE.getServer().getPluginManager().enablePlugin(PowerTools.INSTANCE.getServer().getPluginManager().getPlugin("Vault"));
    }

    public static boolean VaultContains() {
        return PowerTools.INSTANCE.getServer().getPluginManager().getPlugin("Vault") != null;
    }

    public static boolean VaultEnabled() {
        return PowerTools.INSTANCE.getServer().getPluginManager().isPluginEnabled("Vault");
    }

    private static boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = PowerTools.INSTANCE.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private static boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = PowerTools.INSTANCE.getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private static boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = PowerTools.INSTANCE.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Economy econ() {
        return econ;
    }

    public static Chat chat() {
        return chat;
    }

    public static Permission perms() {
        return perms;
    }
}
