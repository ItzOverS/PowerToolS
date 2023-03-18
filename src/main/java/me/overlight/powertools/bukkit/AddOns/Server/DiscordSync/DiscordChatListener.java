package me.overlight.powertools.bukkit.AddOns.Server.DiscordSync;

import me.overlight.powertools.bukkit.PowerTools;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordChatListener
        extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if(!PowerTools.config.getStringList("ServerAddOns.DiscordSync.allowedChannelIDs").contains(e.getChannel().getId())) return;
        if(!e.getMessage().getContentDisplay().startsWith("$$")) return;
        if(!e.getMessage().getContentDisplay().substring(2).split(" ")[0].toLowerCase().equals("execute")) return;

        PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage(e.getMessage().getContentDisplay().substring(e.getMessage().getContentDisplay().split(" ")[0].length()));
    }
}
