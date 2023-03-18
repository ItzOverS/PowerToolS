package me.overlight.powertools.bungee;

import me.overlight.powertools.bukkit.AddOns.Main.AntiBot.BlackListManager;
import me.overlight.powertools.bukkit.Libraries.ColorFormat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;

public class MainEventHandler
        implements Listener {

    @EventHandler
    public void event(PluginMessageEvent e) {
        String channel = e.getTag();
        String[] content = new String(e.getData(), StandardCharsets.UTF_8).substring(2).split("\\|");

        if (!channel.equals("pts:bungee")) return;

        switch (content[0]) {
            case "kick":
                PowerTools.INSTANCE.getProxy().getPlayer(content[1]).disconnect(new ComponentBuilder(ColorFormat.formatColorBungee(content[2])).create());
                break;
        }
    }

    @EventHandler
    public void event(PlayerHandshakeEvent e) {
        String ip = NetworkChecker.getPlayerIPv4(e.getConnection());
        if (e.getHandshake().getRequestedProtocol() > 2) {
            e.getHandshake().setRequestedProtocol(2);
            BlackListManager.blackList(ip);
        }
    }

    @EventHandler
    public void event(ProxyPingEvent e) {
        if (BlackListManager.isBlackList(NetworkChecker.getPlayerIPv4(e.getConnection())))
            e.setResponse(new ServerPing(new ServerPing.Protocol(null, 1), new ServerPing.Players(-1, -1, new ServerPing.PlayerInfo[]{new ServerPing.PlayerInfo(null, "-1")}), ChatColor.RED + "You got BlackListed by PowerAB", PowerTools.INSTANCE.getProxy().getConfig().getFaviconObject()));
    }

    public String[] subStringList(String[] items, int start) {
        String[] result = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            result[i] = items[i].substring(start);
        }
        return result;
    }
}
