package me.overlight.powertools.spigot.AddOns.Main;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.Discord.WebHooks.DiscordAPI;
import me.overlight.powertools.spigot.Modules.mods.PlayTime;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PingCheck
        extends AddOn
        implements Runnable {
    public PingCheck() {
        super("PingCheck", "1.0", "check players ping & kick high pinged players", PowerTools.config.getBoolean("PingCheck.enabled"));
    }

    @Override
    public void run() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (PacketEvents.get().getPlayerUtils().getPing(player) <= PowerTools.config.getInt(this.getName() + ".maxPing"))
                continue;
            final String thisName = this.getName();
            if (PlayTime.PlayTime.get(player.getName()).minute > 5) {
                return;
            }
            Bukkit.getScheduler().runTaskAsynchronously(PowerTools.INSTANCE, new Runnable() {

                @Override
                public void run() {
                    PowerTools.kick(player, PlInfo.KICK_PREFIX + ChatColor.RED + "Your ping is too high - " + PacketEvents.get().getPlayerUtils().getPing(player) + "ms > " + PowerTools.config.getInt(thisName + ".maxPing") + "ms");
                    PowerTools.Alert(PowerTools.Target.STAFF, ChatColor.RED + player.getName() + ChatColor.GOLD + " kicked for high ping");
                    if (PowerTools.config.getBoolean(thisName + ".alert-on-discord")) {
                        DiscordAPI.sendEmbedOnWebhook(player.getName() + " kicked - HighPing", player.getName() + " kicked for has unstable ping on server");
                    }
                }
            });
        }
    }
}
