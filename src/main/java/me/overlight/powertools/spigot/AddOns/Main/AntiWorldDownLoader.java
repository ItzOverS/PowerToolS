package me.overlight.powertools.spigot.AddOns.Main;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.Discord.WebHooks.DiscordAPI;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.PowerTools;
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
        if (this.enabled()) {
            if (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_12)) {
                PowerTools.INSTANCE.getServer().getMessenger().registerIncomingPluginChannel(PowerTools.INSTANCE, "wdl:init", this);
                PowerTools.INSTANCE.getServer().getMessenger().registerOutgoingPluginChannel(PowerTools.INSTANCE, "wdl:control");
            } else {
                PowerTools.INSTANCE.getServer().getMessenger().registerIncomingPluginChannel(PowerTools.INSTANCE, "WDL|INIT", this);
                PowerTools.INSTANCE.getServer().getMessenger().registerOutgoingPluginChannel(PowerTools.INSTANCE, "WDL|CONTROL");
            }
            PowerTools.INSTANCE.getServer().getPluginManager().registerEvents(this, PowerTools.INSTANCE);
        }
    }

    @Override
    public void onDisabled() {
        if (this.isEnabled()) {
            if (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_12)) {
                PowerTools.INSTANCE.getServer().getMessenger().unregisterIncomingPluginChannel(PowerTools.INSTANCE, "wdl:init", this);
                PowerTools.INSTANCE.getServer().getMessenger().unregisterOutgoingPluginChannel(PowerTools.INSTANCE, "wdl:control");
            } else {
                PowerTools.INSTANCE.getServer().getMessenger().unregisterIncomingPluginChannel(PowerTools.INSTANCE, "WDL|INIT", this);
                PowerTools.INSTANCE.getServer().getMessenger().unregisterOutgoingPluginChannel(PowerTools.INSTANCE, "WDL|CONTROL");
            }
        }
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
