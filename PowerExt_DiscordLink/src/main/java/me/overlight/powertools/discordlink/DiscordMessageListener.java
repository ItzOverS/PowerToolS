package me.overlight.powertools.discordlink;

import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerTools;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DiscordMessageListener
        extends ListenerAdapter {
    public DiscordMessageListener(PowerExt powerExtDiscordLink) {

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage("Called Ev 1");
        String m = event.getMessage().getContentDisplay();
        if(m.split(" ").length != 2) return;
        String code = m.split(" ")[1].trim();
        String author = event.getAuthor().getName();
        PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage("Called Ev 2");
        if(!m.startsWith(PowerExt.dsPrefix)) return;
        String mcUsername = getKey(code);
        PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage("Called Ev 3");
        if(mcUsername == null) return;
        PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage("Called Ev 4");
        if(Bukkit.getPlayer(mcUsername) == null){
            PowerExt.playerCodes.remove(mcUsername);
            PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage("Called Ev 5");
            return;
        }
        try {
            PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage("Called Ev 6");
            event.getMessage().delete();
            PowerExt.discordIDsUser.put(mcUsername, author);
            Bukkit.getPlayer(mcUsername).sendMessage(PlInfo.PREFIX + ChatColor.GREEN + "You has been simplify verified");
        } catch(Exception e) { }
    }

    public String getKey(String value){
        for (String s : PowerExt.playerCodes.keySet()) {
            if(Objects.equals(PowerExt.playerCodes.get(s), value))
                return s;
        }
        return null;
    }
}
