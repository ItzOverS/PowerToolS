package me.overlight.powertools.bukkit.AddOns.Server;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.Modules.mods.Channel;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class PublicChat
        extends AddOn
        implements PluginMessageListener, Listener {
    public PublicChat() {
        super("ServerAddOns.PublicChat", "1.0", "Make all servers chat get one", PowerTools.config.getBoolean("ServerAddOns.PublicChat.enabled"), "pts:bungee");
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if(!s.equals("pts:bungee")) return;
        String content = new String(bytes, StandardCharsets.UTF_8);
        player.sendMessage(content);
        if(!content.startsWith("chat|")) return;
        String sender = content.split("\\|")[1],
                message = content.split("\\|")[2].replace("兆", "|");

        for(Player p: Bukkit.getOnlinePlayers()){ //Bukkit.getOnlinePlayers() only get online players in current server ( spigot / craft-bukkit )
            p.sendMessage("<" + sender + "> " + message);
        }
    }

    @EventHandler
    public void event(AsyncPlayerChatEvent e){
        if(!Objects.equals(Channel.ChatChannel.get(e.getPlayer().getName()), "ALL")) return;

        e.setCancelled(true);
        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        data.writeUTF("chat|" + e.getPlayer() + "|" + e.getMessage().replace("|", "兆"));
        e.getPlayer().sendPluginMessage(PowerTools.INSTANCE, "pts:bungee", data.toByteArray());
    }
}
