package me.overlight.powertools;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import me.overlight.powertools.AddOns.AddOnManager;
import me.overlight.powertools.AddOns.Bedwars.AntiTeamUp;
import me.overlight.powertools.AddOns.Bedwars.FireBallKnockback;
import me.overlight.powertools.AddOns.Bedwars.TntKnockback;
import me.overlight.powertools.AddOns.Hub.KnockbackPlate;
import me.overlight.powertools.AddOns.Hub.VoidTP;
import me.overlight.powertools.AddOns.Main.*;
import me.overlight.powertools.AddOns.Server.AntiRejoin;
import me.overlight.powertools.AddOns.Server.BanMOTD;
import me.overlight.powertools.AddOns.Server.ForcePing;
import me.overlight.powertools.AddOns.Server.RandomMOTD;
import me.overlight.powertools.AddOns.Survival.NoRespawn;
import me.overlight.powertools.AddOns.Survival.RandomSpawn;
import me.overlight.powertools.Command.MainCommand;
import me.overlight.powertools.Libraries.ColorFormat;
import me.overlight.powertools.Libraries.PlaceHolders;
import me.overlight.powertools.Libraries.WebHooks.DiscordAPI;
import me.overlight.powertools.Libraries.WebHooks.DiscordWebhook;
import me.overlight.powertools.Modules.ModuleManager;
import me.overlight.powertools.Modules.mods.*;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.Plugin.PlMessages;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class PowerTools
        extends JavaPlugin {
    public static PowerTools INSTANCE;
    public static FileConfiguration config;
    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEvents.get().getSettings()
                .checkForUpdates(false)
                .bStats(true)
                .fallbackServerVersion(PacketEvents.get().getServerUtils().getVersion());
        PacketEvents.get().load();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
        PowerTools.config = getConfig();

        getServer().getPluginCommand("powertools").setExecutor(new MainCommand());
        getServer().getPluginCommand("powertools").setTabCompleter(new MainCommand());

        discordWebhookConnection:
        {
            if (PowerTools.config.getBoolean("discordWebhook.enabled")) {
                String url = PowerTools.config.getString("discordWebhook.url");
                if (url == null || url.equals(""))
                    break discordWebhookConnection;
                getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.GOLD + "Connecting to discord webhook");
                try {
                    DiscordWebhook discordWebhook = new DiscordWebhook(url);
                    discordWebhook.setUsername("PowerToolS");
                    discordWebhook.setAvatarUrl("https://s6.uupload.ir/files/icon_zbxl.png");
                    discordWebhook.setContent(":white_check_mark: **Simplify connected to DiscordWebHook**");
                    discordWebhook.execute();
                    getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.GREEN + "Simplify connected to webhook");
                    DiscordAPI.DiscordWebHookURL = url;
                } catch(Exception ex){
                    getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.RED + "Something went wrong: " + ex.getMessage());
                    DiscordAPI.DiscordWebHookURL = null;
                }
            }
        }

        ModuleManager.registerModule(new Knockback(), new Freeze(), new Channel(), new MemoryUsage(), new Protect(), new Rotate(), new PlayTime(), new Vanish());
        ModuleManager.loadModulesData();

        AddOnManager.registerAddOn(new AfkCheck(), new AntiWorldDownLoader(), new CpsCheck(), new PingCheck(), new ChatManager(), new ForceSpawn(), new JoinMessage(),
                new QuitMessage(), new UserNameManager(), new CommandDeny(), new PvpManager(), new PvpRegisterer(), new VersionCheck(), new WorldEnvironments(), new ChatFormat());
        if(config.getBoolean("BedwarsAddOns.enabled"))
            AddOnManager.registerAddOn(new AntiTeamUp(), new TntKnockback(), new FireBallKnockback());
        if(config.getBoolean("HubAddOns.enabled"))
            AddOnManager.registerAddOn(new KnockbackPlate(), new VoidTP());
        if(config.getBoolean("SurvivalAddOns.enabled"))
            AddOnManager.registerAddOn(new ChatManager(), new NoRespawn(), new RandomSpawn());
        if(config.getBoolean("ServerAddOns.enabled"))
            AddOnManager.registerAddOn(new RandomMOTD(), new BanMOTD(), new AntiRejoin(), new ForcePing());
        AddOnManager.loadAddons();

        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("   @color_dark_green___  @color_aqua__________   "));
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("  @color_dark_green/ _ \\@color_aqua/_  __/ __/ @color_dark_gray Welcome to PowerToolS v" + PlInfo.VERSION));
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor(" @color_dark_green/ ___/ @color_aqua/ / _\\ \\   @color_dark_gray Running on Spigot/Bukkit"));
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("@color_dark_green/_/    @color_aqua/_/ /___/  @color_dark_gray  by ItzOver"));
        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage("");


        try{
            getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_goldChecking for updates"));
            if(!UpdateChecker.isUpToDate()){
                getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_greenThere is a newer version available"));
                getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_greenDownload it from: " + UpdateChecker.getDownloadLink()));
            } else {
                getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_greenPlugin is up to date"));
            }
        } catch (IOException | ParseException e) {
            getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_redFailed to check for updates | check your network connection"));
        }

        if(!new PlaceHolders().isRegistered()) {
            new PlaceHolders().register();
            getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_greenSuccess registered placeholders"));
        }
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();

        if(PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_12)){
            getServer().getMessenger().unregisterIncomingPluginChannel(this, "wdl:init");
            getServer().getMessenger().unregisterOutgoingPluginChannel(this, "wdl:control");
        } else {
            getServer().getMessenger().unregisterIncomingPluginChannel(this, "WDL|INIT");
            getServer().getMessenger().unregisterOutgoingPluginChannel(this, "WDL|CONTROL");
        }
        getServer().getMessenger().unregisterIncomingPluginChannel(this, (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_12))?"mc:brand":"MC|BRAND");
    }

    public enum Target{
        STAFF,
        MEMBERS
    }
    public static void Alert(Target targ, String msg){
        for(Player player: Bukkit.getOnlinePlayers()){
            String message = (!msg.startsWith(PlInfo.PREFIX) ? PlInfo.PREFIX : "") + msg;
            if(targ == Target.STAFF) {
                if (player.isOp())
                    player.sendMessage(message);
            } else {
                player.sendMessage(message);

            }
        }
    }
}
