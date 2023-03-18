package me.overlight.powertools.bukkit.AddOns.Main;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.Discord.WebHooks.DiscordAPI;
import me.overlight.powertools.bukkit.Plugin.PlInfo;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class AntiWorldDownLoader
        extends AddOn
        implements Listener, PluginMessageListener {
    public AntiWorldDownLoader() {
        super("AntiWorldDownLoader", "1.0", "prevent players from download server's maps using map downloaders", PowerTools.config.getBoolean("AntiWorldDownLoader.enabled"));
    }

    @Override
    public void onEnabled() {
        PowerTools.INSTANCE.getServer().getMessenger().registerIncomingPluginChannel(PowerTools.INSTANCE, PowerTools.handleChannel("WDL|INIT"), this);
        PowerTools.INSTANCE.getServer().getMessenger().registerOutgoingPluginChannel(PowerTools.INSTANCE, PowerTools.handleChannel("WDL|CONTROL"));
    }

    @Override
    public void onDisabled() {
        PowerTools.INSTANCE.getServer().getMessenger().unregisterIncomingPluginChannel(PowerTools.INSTANCE, PowerTools.handleChannel("WDL|INIT"));
        PowerTools.INSTANCE.getServer().getMessenger().unregisterOutgoingPluginChannel(PowerTools.INSTANCE, PowerTools.handleChannel("WDL|CONTROL"));
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] data) {
        if (channel.equals("WDL|INIT")) {
            String thisName = this.getName();
            if (!player.isOp()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                    PowerTools.kick(player, PlInfo.KICK_PREFIX + ChatColor.RED + "I think you're using WorldDownLoader");
                    if (PowerTools.config.getBoolean(thisName + ".alert-on-discord"))
                        DiscordAPI.sendEmbedOnWebhook(player.getName() + " kicked - Using **W**orld**D**own**L**oader", "I think " + player.getName() + " using **W**orld**D**own**L**oader");
                }, 100);
            } else {
                if (!PowerTools.config.getBoolean("AntiWorldDownLoader.continueOperators")) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(PowerTools.INSTANCE, () -> {
                        PowerTools.kick(player, PlInfo.KICK_PREFIX + ChatColor.RED + "I think you're using WorldDownLoader");
                        if (PowerTools.config.getBoolean(thisName + ".alert-on-discord"))
                            DiscordAPI.sendEmbedOnWebhook("OP:" + player.getName() + " kicked - Using **W**orld**D**own**L**oader", "I think OP:" + player.getName() + " using **W**orld**D**own**L**oader");
                    }, 100);
                }
            }
        }
    }
}
