package me.overlight.powertools.AddOns.Main;

import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.Libraries.WebHooks.DiscordAPI;
import me.overlight.powertools.PowerTools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.File;

public class CommandDeny
        extends AddOn
        implements Listener {
    public CommandDeny() {
        super("CommandDeny", "1.0", "deny commands", PowerTools.config.getBoolean("CommandDeny.enabled"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandExecute(PlayerCommandPreprocessEvent e) {
        String unkMessage = ChatColor.translateAlternateColorCodes('&', YamlConfiguration.loadConfiguration(new File("spigot.yml")).getString("messages.unknown-command"));
        String command = e.getMessage().split(" ")[0].substring(1);
        if (PowerTools.config.getBoolean(this.getName() + ".denyPluginNameOnStart")) {
            if (command.contains(":")) {
                if (PowerTools.config.getStringList(this.getName() + ".commands").contains(command.split(":")[1])) {
                    if (!e.getPlayer().isOp()) {
                        PowerTools.Alert(PowerTools.Target.STAFF, ChatColor.RED + e.getPlayer().getName() + ChatColor.GOLD + " failed to execute: " + e.getMessage().substring(1));
                        e.getPlayer().sendMessage(unkMessage);
                        e.setCancelled(true);
                        if (PowerTools.config.getBoolean(this.getName() + ".alert-on-discord"))
                            DiscordAPI.sendEmbedOnWebhook(e.getPlayer().getName() + " failed " + e.getMessage(), e.getPlayer().getName() + " has failed to execute " + e.getMessage());
                    } else {
                        if (!PowerTools.config.getBoolean(this.getName() + ".ignoreOps")) {
                            PowerTools.Alert(PowerTools.Target.STAFF, ChatColor.RED + e.getPlayer().getName() + ChatColor.GOLD + " failed to execute: " + e.getMessage().substring(1));
                            e.getPlayer().sendMessage(unkMessage);
                            e.setCancelled(true);
                            if (PowerTools.config.getBoolean(this.getName() + ".alert-on-discord"))
                                DiscordAPI.sendEmbedOnWebhook("OP:" + e.getPlayer().getName() + " failed " + e.getMessage(), "OP:" + e.getPlayer().getName() + " has failed to execute " + e.getMessage());
                        }
                    }
                }
            } else {
                if (PowerTools.config.getStringList(this.getName() + ".commands").contains(command)) {
                    if (!e.getPlayer().isOp()) {
                        PowerTools.Alert(PowerTools.Target.STAFF, ChatColor.RED + e.getPlayer().getName() + ChatColor.GOLD + " failed to execute: " + e.getMessage().substring(1));
                        e.getPlayer().sendMessage(unkMessage);
                        e.setCancelled(true);
                        if (PowerTools.config.getBoolean(this.getName() + ".alert-on-discord"))
                            DiscordAPI.sendEmbedOnWebhook(e.getPlayer().getName() + " failed " + e.getMessage(), e.getPlayer().getName() + " has failed to execute " + e.getMessage());
                    } else {
                        if (!PowerTools.config.getBoolean(this.getName() + ".ignoreOps")) {
                            PowerTools.Alert(PowerTools.Target.STAFF, ChatColor.RED + e.getPlayer().getName() + ChatColor.GOLD + " failed to execute: " + e.getMessage().substring(1));
                            e.getPlayer().sendMessage(unkMessage);
                            e.setCancelled(true);
                            if (PowerTools.config.getBoolean(this.getName() + ".alert-on-discord"))
                                DiscordAPI.sendEmbedOnWebhook("OP:" + e.getPlayer().getName() + " failed " + e.getMessage(), "OP:" + e.getPlayer().getName() + " has failed to execute " + e.getMessage());
                        }
                    }
                }
            }
        } else {
            if (PowerTools.config.getStringList(this.getName() + ".commands").contains(command)) {
                if (!e.getPlayer().isOp()) {
                    PowerTools.Alert(PowerTools.Target.STAFF, ChatColor.RED + e.getPlayer().getName() + ChatColor.GOLD + " failed to execute: " + e.getMessage().substring(1));
                    e.getPlayer().sendMessage(unkMessage);
                    e.setCancelled(true);
                    if (PowerTools.config.getBoolean(this.getName() + ".alert-on-discord"))
                        DiscordAPI.sendEmbedOnWebhook(e.getPlayer().getName() + " failed " + e.getMessage(), e.getPlayer().getName() + " has failed to execute " + e.getMessage());
                } else {
                    if (!PowerTools.config.getBoolean(this.getName() + ".ignoreOps")) {
                        PowerTools.Alert(PowerTools.Target.STAFF, ChatColor.RED + e.getPlayer().getName() + ChatColor.GOLD + " failed to execute: " + e.getMessage().substring(1));
                        e.getPlayer().sendMessage(unkMessage);
                        e.setCancelled(true);
                        if (PowerTools.config.getBoolean(this.getName() + ".alert-on-discord"))
                            DiscordAPI.sendEmbedOnWebhook("OP:" + e.getPlayer().getName() + " failed " + e.getMessage(), "OP:" + e.getPlayer().getName() + " has failed to execute " + e.getMessage());
                    }
                }
            }
        }
    }
}
