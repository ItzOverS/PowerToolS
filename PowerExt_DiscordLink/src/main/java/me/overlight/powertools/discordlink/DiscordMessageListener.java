package me.overlight.powertools.discordlink;

import me.overlight.powertools.Libraries.PluginYaml;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerTools;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DiscordMessageListener
        extends ListenerAdapter {
    public DiscordMessageListener(PowerExt powerExtDiscordLink) {

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getChannel().getId().equals(PowerTools.config.getString(PowerExt.module.getConfigName() + ".channelID")))
            return;
        String m = event.getMessage().getContentDisplay();
        if (m.split(" ").length != 2) return;
        String code = m.split(" ")[1].trim();
        String author = event.getAuthor().getName();
        if (!m.startsWith(PowerExt.dsPrefix)) return;
        String mcUsername = getKey(code);
        if (mcUsername == null) return;
        if (Bukkit.getPlayer(mcUsername) == null) {
            PowerExt.playerCodes.remove(mcUsername);
            return;
        }
        try {
            AuditableRestAction<Void> result = event.getMessage().delete();
            PowerExt.discordIDsUser.put(mcUsername, author);
            Bukkit.getPlayer(mcUsername).sendMessage(PlInfo.PREFIX + ChatColor.GREEN + "You has been simplify verified");
            YamlConfiguration yml = new PluginYaml("discordLinks").getYaml();
            yml.set(mcUsername, author);
            new PluginYaml("discordLinks").setYaml(yml).saveYaml();
        } catch (Exception ignored) {
        }
    }

    public String getKey(String value) {
        for (String s : PowerExt.playerCodes.keySet()) {
            if (Objects.equals(PowerExt.playerCodes.get(s), value))
                return s;
        }
        return null;
    }
}
