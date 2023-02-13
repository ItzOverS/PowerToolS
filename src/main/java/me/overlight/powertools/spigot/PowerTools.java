package me.overlight.powertools.spigot;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import me.overlight.powertools.spigot.APIs.UpdateChecker;
import me.overlight.powertools.spigot.APIs.Vault;
import me.overlight.powertools.spigot.AddOns.AddOnManager;
import me.overlight.powertools.spigot.AddOns.Bedwars.AntiTeamUp;
import me.overlight.powertools.spigot.AddOns.Bedwars.FireBallKnockback;
import me.overlight.powertools.spigot.AddOns.Bedwars.TntKnockback;
import me.overlight.powertools.spigot.AddOns.Hub.KnockbackPlate;
import me.overlight.powertools.spigot.AddOns.Hub.VoidTP;
import me.overlight.powertools.spigot.AddOns.Main.*;
import me.overlight.powertools.spigot.AddOns.Main.AntiBot.AntiBot;
import me.overlight.powertools.spigot.AddOns.Main.Captcha.Captcha;
import me.overlight.powertools.spigot.AddOns.Main.PvpRegisterer.PvpRegisterer;
import me.overlight.powertools.spigot.AddOns.Render.ScoreBoards;
import me.overlight.powertools.spigot.AddOns.Render.TabList;
import me.overlight.powertools.spigot.AddOns.Server.AntiRejoin;
import me.overlight.powertools.spigot.AddOns.Server.BanMOTD;
import me.overlight.powertools.spigot.AddOns.Server.DiscordSync.DiscordSync;
import me.overlight.powertools.spigot.AddOns.Server.ForcePing;
import me.overlight.powertools.spigot.AddOns.Server.PluginHider.PluginHider;
import me.overlight.powertools.spigot.AddOns.Server.RandomMOTD;
import me.overlight.powertools.spigot.AddOns.Survival.FallingBlocks;
import me.overlight.powertools.spigot.AddOns.Survival.NoRespawn;
import me.overlight.powertools.spigot.AddOns.Survival.RandomSpawn;
import me.overlight.powertools.spigot.Command.MainCommand;
import me.overlight.powertools.spigot.Command.TabComplete;
import me.overlight.powertools.spigot.Discord.Bot;
import me.overlight.powertools.spigot.Discord.WebHooks.DiscordAPI;
import me.overlight.powertools.spigot.Discord.WebHooks.DiscordWebhook;
import me.overlight.powertools.spigot.Libraries.ColorFormat;
import me.overlight.powertools.spigot.Libraries.PlaceHolders;
import me.overlight.powertools.spigot.Modules.ModuleManager;
import me.overlight.powertools.spigot.Modules.mods.*;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import me.overlight.powertools.spigot.PowerModules.ExtensionManager;
import me.overlight.powertools.spigot.PowerModules.PluginEnabledEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
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
    private static String err = "";

    @Override
    public void onLoad() {
        INSTANCE = this;
        ((Logger) LogManager.getRootLogger()).addFilter(new ConsoleMessageFilter());
        PacketEvents.create(this);
        PacketEvents.get().getSettings()
                .checkForUpdates(false)
                .bStats(true)
                .fallbackServerVersion(PacketEvents.get().getServerUtils().getVersion());
        PacketEvents.get().load();
    }

    @Override
    public void onEnable() {
        try {
            Alert(Target.CONSOLE, "@color_greenEnabling " + PlInfo.INV_PREFIX.substring(0, PlInfo.INV_PREFIX.length() - 11));
            INSTANCE = this;
            saveDefaultConfig();

            PowerTools.config = getConfig();

            if (Vault.implementAPI()) {
                getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.GREEN + "simplify connected to vault options");
            }

            getServer().getPluginCommand("powertools").setExecutor(new MainCommand());
            getServer().getPluginCommand("powertools").setTabCompleter(new TabComplete());

            discordWebhookConnection:
            {
                if (config.getBoolean("Discord.WebHook.enabled")) {
                    String url = config.getString("Discord.WebHook.url");
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
                    } catch (Exception ex) {
                        getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.RED + "Something went wrong: " + ex.getMessage());
                        DiscordAPI.DiscordWebHookURL = null;
                    }
                }
            }

            {
                if (!config.getString("Discord.Bot.token").replace(" ", "").equals("")) {
                    getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.GOLD + "Logging in to Discord-Bot");
                    Bot.loginClient(config.getString("Discord.Bot.token"));
                }
            }

            ModuleManager.registerModule(new Knockback(), new Freeze(), new Channel(), new MemoryUsage(), new Protect(), new Rotate(), new PlayTime(), new Vanish(), new Toggle(), new CpsMap());
            ModuleManager.loadModulesData();

            loadAddOns();
            loadExtensions();

            try {
                getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_goldChecking for updates"));
                if (UpdateChecker.canCheckForUpdate()) {
                    if (!UpdateChecker.isUpToDate()) {
                        getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_greenThere is a newer version available"));
                        getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_greenDownload it from: " + UpdateChecker.getDownloadLink()));
                    } else {
                        getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_greenPlugin is up to date"));
                    }
                } else {
                    getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_redFailed to check for updates | check your network connection"));
                }
            } catch (IOException | ParseException e) {
                getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_redFailed to check for updates | check your network connection"));
            }

            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                if (!new PlaceHolders().isRegistered()) {
                    if (new PlaceHolders().register())
                        getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_greenSuccess registered placeholders"));
                    else
                        getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_redFailed to register placeholders"));
                }
            }

            if (!PacketEvents.get().isInitialized() && !PacketEvents.get().isInitializing()) {
                PacketEvents.get().init();
            }

            getServer().getPluginManager().registerEvents(new MainEventHandler(), this);
            getServer().getPluginManager().registerEvents(new PluginEnabledEvent(), this);

            getServer().getMessenger().registerOutgoingPluginChannel(this, "pts:bungee");

            getServer().getConsoleSender().sendMessage("");
            getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("   @color_dark_green___  @color_aqua__________   "));
            getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("  @color_dark_green/ _ \\@color_aqua/_  __/ __/ @color_dark_gray Welcome to PowerToolS v" + PlInfo.VERSION));
            getServer().getConsoleSender().sendMessage(ColorFormat.formatColor(" @color_dark_green/ ___/ @color_aqua/ / _\\ \\   @color_dark_gray Running on Spigot/Bukkit"));
            getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("@color_dark_green/_/    @color_aqua/_/ /___/  @color_dark_gray  by ItzOver"));
            getServer().getConsoleSender().sendMessage("");
            getServer().getConsoleSender().sendMessage("");
        } catch (Exception e) {
            err = "Err: " + e.getMessage();
            e.printStackTrace();
            Alert(Target.CONSOLE, "@color_redAn error occurred while enabling plugin");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        Alert(Target.CONSOLE, "@color_redDisabling @color_dark_redPower@color_redToolS");

        PacketEvents.get().terminate();

        if (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_12)) {
            getServer().getMessenger().unregisterIncomingPluginChannel(this, "wdl:init");
            getServer().getMessenger().unregisterOutgoingPluginChannel(this, "wdl:control");
        } else {
            getServer().getMessenger().unregisterIncomingPluginChannel(this, "WDL|INIT");
            getServer().getMessenger().unregisterOutgoingPluginChannel(this, "WDL|CONTROL");
        }
        getServer().getMessenger().unregisterIncomingPluginChannel(this, (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_12)) ? "mc:brand" : "MC|BRAND");

        AddOnManager.unRegisterAll();

        ExtensionManager.removeAllExtensions();

        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("   @color_dark_red___  @color_red__________   "));
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("  @color_dark_red/ _ \\@color_red/_  __/ __/ @color_dark_gray " + err));
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor(" @color_dark_red/ ___/ @color_red/ / _\\ \\   @color_dark_gray Disabled PowerToolS v" + PlInfo.VERSION));
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("@color_dark_red/_/    @color_red/_/ /___/  @color_dark_gray  by ItzOver"));
        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage("");
    }

    public enum Target {
        STAFF,
        MEMBERS,
        CONSOLE
    }

    public static void Alert(Target targ, String msg) {
        String s = (!msg.startsWith(PlInfo.PREFIX) ? PlInfo.PREFIX : "") + msg;
        if (targ == Target.CONSOLE) {
            if(PowerTools.INSTANCE != null)
                PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage(ColorFormat.formatColor(s));
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (targ == Target.STAFF) {
                if (player.isOp())
                    player.sendMessage(s);
            } else {
                player.sendMessage(s);
            }
        }
    }
    public static void Alert(Target targ, String msg, boolean insertPrefix) {
        String s = msg;
        if(insertPrefix)
            s = (!msg.startsWith(PlInfo.PREFIX) ? PlInfo.PREFIX : "") + msg;
        if (targ == Target.CONSOLE) {
            if(PowerTools.INSTANCE != null)
                PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage(ColorFormat.formatColor(s));
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (targ == Target.STAFF) {
                if (player.isOp())
                    player.sendMessage(s);
            } else {
                player.sendMessage(s);
            }
        }
    }

    public static void kick(Player player, String reason) {
        if (PowerTools.config.getBoolean("bungeecord")) {
            ByteArrayDataOutput data = ByteStreams.newDataOutput();
            data.writeUTF("kick|" + player.getName() + "|" + reason);
            player.sendPluginMessage(PowerTools.INSTANCE, "pts:bungee", data.toByteArray());
        } else {
            Bukkit.getScheduler().runTask(PowerTools.INSTANCE, () -> {
                player.kickPlayer(reason);
            });
        }
    }

    public static void loadAddOns() {
        AddOnManager.registerAddOn(new AfkCheck(), new AntiWorldDownLoader(), new CpsCheck(), new PingCheck(), new ChatManager(), new ForceSpawn(), new JoinMessage(), new CommandRedirect(),
                new QuitMessage(), new UserNameManager(), new CommandDeny(), new PvpManager(), new PvpRegisterer(), new VersionCheck(), new WorldEnvironments(), new ChatFormat(),
                new SlashServer(), new Captcha(), new NetworkChecker(), new AntiBot());
        if (config.getBoolean("BedwarsAddOns.enabled"))
            AddOnManager.registerAddOn(new AntiTeamUp(), new TntKnockback(), new FireBallKnockback());
        if (config.getBoolean("HubAddOns.enabled"))
            AddOnManager.registerAddOn(new KnockbackPlate(), new VoidTP());
        if (config.getBoolean("SurvivalAddOns.enabled"))
            AddOnManager.registerAddOn(new ChatManager(), new NoRespawn(), new RandomSpawn(), new FallingBlocks());
        if (config.getBoolean("ServerAddOns.enabled"))
            AddOnManager.registerAddOn(new RandomMOTD(), new BanMOTD(), new AntiRejoin(), new ForcePing(), new PluginHider(), new DiscordSync());
        if (config.getBoolean("RenderAddOns.enabled"))
            AddOnManager.registerAddOn(new ScoreBoards(), new TabList());
        AddOnManager.loadAddons();
    }

    public static void loadExtensions() {
        for (String ExName : config.getStringList("Extensions")) {
            try {
                ExtensionManager.hookInto(ExName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
