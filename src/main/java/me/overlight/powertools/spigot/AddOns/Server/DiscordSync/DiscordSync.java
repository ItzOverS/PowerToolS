package me.overlight.powertools.spigot.AddOns.Server.DiscordSync;

import me.overlight.powertools.spigot.AddOns.AddOn;
import me.overlight.powertools.spigot.Discord.Bot;
import me.overlight.powertools.spigot.PowerTools;

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
