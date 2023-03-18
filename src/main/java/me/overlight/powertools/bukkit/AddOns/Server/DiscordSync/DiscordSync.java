package me.overlight.powertools.bukkit.AddOns.Server.DiscordSync;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.Discord.Bot;
import me.overlight.powertools.bukkit.PowerTools;

public class DiscordSync
        extends AddOn {
    public DiscordSync() {
        super("ServerAddOns.DiscordSync", "1.0", "Sync your minecraft server with discord", PowerTools.config.getBoolean("ServerAddOns.DiscordSync.enabled"));
    }

    @Override
    public void onEnabled() {
        Bot.getClient().addEventListener(new DiscordChatListener());
    }
}
