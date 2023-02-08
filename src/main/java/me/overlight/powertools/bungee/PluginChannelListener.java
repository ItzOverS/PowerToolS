package me.overlight.powertools.bungee;

import me.overlight.powertools.spigot.Libraries.ColorFormat;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;

public class PluginChannelListener
        implements Listener {

    @EventHandler
    public void event(PluginMessageEvent e){
        String channel = e.getTag();
        String[] content = new String(e.getData(), StandardCharsets.UTF_8).substring(2).split("\\|");

        if(!channel.equals("pts:bungee")) return;

        switch(content[0]) {
            case "kick":
                PowerTools.INSTANCE.getProxy().getPlayer(content[1]).disconnect(new ComponentBuilder(ColorFormat.formatColorBungee(content[2])).create());
                break;
        }
    }

    public String[] subStringList(String[] items, int start){
        String[] result = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            result[i] = items[i].substring(start);
        }
        return result;
    }
}
