package me.overlight.powertools.bungee.AddOns;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class AntiProtocolDetect
        implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void event(ProxyPingEvent e) {
        e.setResponse(new ServerPing(new ServerPing.Protocol("NOT YOU BUSINESS", e.getResponse().getVersion().getProtocol()), e.getResponse().getPlayers(), e.getResponse().getDescription(), e.getResponse().getFaviconObject()));
    }
}
