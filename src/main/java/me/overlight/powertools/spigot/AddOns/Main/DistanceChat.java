package me.overlight.powertools.spigot.AddOns.Main;

import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.Plugin.PlPerms;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class DistanceChat
        extends AddOn
        implements Listener {
    public DistanceChat() {
        super("DistanceChat", "1.0", "A chat it depend on players distance", PowerTools.config.getBoolean("DistanceChat.enabled"));
    }

    @EventHandler
    public void event(AsyncPlayerChatEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p == e.getPlayer()) continue;
            if (p.hasPermission(PlPerms.Perms.DistanceChatBypass.get())) return;
            if (p.getLocation().distance(e.getPlayer().getLocation()) >= PowerTools.config.getDouble(this.getName() + ".maxDistance")) {
                e.getRecipients().remove(p);
            }
        }
    }
}
