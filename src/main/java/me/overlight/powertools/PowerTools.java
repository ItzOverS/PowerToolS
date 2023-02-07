package me.overlight.powertools;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import me.overlight.powertools.APIs.UpdateChecker;
import me.overlight.powertools.APIs.Vault;
import me.overlight.powertools.AddOns.AddOnManager;
import me.overlight.powertools.AddOns.Bedwars.AntiTeamUp;
import me.overlight.powertools.AddOns.Bedwars.FireBallKnockback;
import me.overlight.powertools.AddOns.Bedwars.TntKnockback;
import me.overlight.powertools.AddOns.Hub.KnockbackPlate;
import me.overlight.powertools.AddOns.Hub.VoidTP;
import me.overlight.powertools.AddOns.Main.*;
import me.overlight.powertools.AddOns.Main.Captcha.Captcha;
import me.overlight.powertools.AddOns.Main.NetworkChecker;
import me.overlight.powertools.AddOns.Main.PvpRegisterer.PvpRegisterer;
import me.overlight.powertools.AddOns.Render.ScoreBoards;
import me.overlight.powertools.AddOns.Render.TabList;
import me.overlight.powertools.AddOns.Server.AntiRejoin;
import me.overlight.powertools.AddOns.Server.BanMOTD;
import me.overlight.powertools.AddOns.Server.ForcePing;
import me.overlight.powertools.AddOns.Server.RandomMOTD;
import me.overlight.powertools.AddOns.Survival.FallingBlocks;
import me.overlight.powertools.AddOns.Survival.NoRespawn;
import me.overlight.powertools.AddOns.Survival.RandomSpawn;
import me.overlight.powertools.Command.MainCommand;
import me.overlight.powertools.Command.TabComplete;
import me.overlight.powertools.Libraries.ColorFormat;
import me.overlight.powertools.Libraries.PlaceHolders;
import me.overlight.powertools.Libraries.WebHooks.DiscordAPI;
import me.overlight.powertools.Libraries.WebHooks.DiscordWebhook;
import me.overlight.powertools.Modules.ModuleManager;
import me.overlight.powertools.Modules.mods.*;
import me.overlight.powertools.Modules.mods.Freeze;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerModules.ExtensionManager;
import me.overlight.powertools.PowerModules.PluginEnabledEvent;
import me.overlight.powertools.PowerModules.PowerModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PowerTools
        extends JavaPlugin {
    public static PowerTools INSTANCE;
    public static FileConfiguration config;

    @Override
    public void onLoad() {
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
            INSTANCE = this;
            saveDefaultConfig();
            if(!new File("plugins\\PowerToolS\\types.yml").exists())
                saveResource("types.yml", false);

            PowerTools.config = getConfig();

            if(Vault.implementAPI()){
                getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.GREEN + "simplify connected to vault options");
            }

            getServer().getPluginCommand("powertools").setExecutor(new MainCommand());
            getServer().getPluginCommand("powertools").setTabCompleter(new TabComplete());

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
                    } catch (Exception ex) {
                        getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.RED + "Something went wrong: " + ex.getMessage());
                        DiscordAPI.DiscordWebHookURL = null;
                    }
                }
            }

            ModuleManager.registerModule(new Knockback(), new Freeze(), new Channel(), new MemoryUsage(), new Protect(), new Rotate(), new PlayTime(), new Vanish(), new Toggle(), new CpsMap());
            ModuleManager.loadModulesData();

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
                AddOnManager.registerAddOn(new RandomMOTD(), new BanMOTD(), new AntiRejoin(), new ForcePing());
            if (config.getBoolean("RenderAddOns.enabled"))
                AddOnManager.registerAddOn(new ScoreBoards(), new TabList());
            AddOnManager.loadAddons();

            getServer().getPluginManager().registerEvents(new PluginEnabledEvent(), this);

            try { ExtensionManager.hookInto("DiscordLink"); } catch (IOException | ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) { e.printStackTrace(); }
            try { ExtensionManager.hookInto("CommandPanel"); } catch (IOException | ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) { e.printStackTrace(); }
            try { ExtensionManager.hookInto("Profiles"); } catch (IOException | ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) { e.printStackTrace(); }

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

            if (!new PlaceHolders().isRegistered()) {
                if(new PlaceHolders().register())
                    getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_greenSuccess registered placeholders"));
                else
                    getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ColorFormat.formatColor("@color_redFailed to register placeholders"));
            }

            if (!PacketEvents.get().isInitialized() && !PacketEvents.get().isInitializing()) {
                PacketEvents.get().init();
            }

            getServer().getConsoleSender().sendMessage("");
            getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("   @color_dark_green___  @color_aqua__________   "));
            getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("  @color_dark_green/ _ \\@color_aqua/_  __/ __/ @color_dark_gray Welcome to PowerToolS v" + PlInfo.VERSION));
            getServer().getConsoleSender().sendMessage(ColorFormat.formatColor(" @color_dark_green/ ___/ @color_aqua/ / _\\ \\   @color_dark_gray Running on Spigot/Bukkit"));
            getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("@color_dark_green/_/    @color_aqua/_/ /___/  @color_dark_gray  by ItzOver"));
            getServer().getConsoleSender().sendMessage("");
            getServer().getConsoleSender().sendMessage("");
        } catch(Exception e){
            getServer().getPluginManager().disablePlugin(this);
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

        AddOnManager.unRegisterAddOn();

        ExtensionManager.removeAllExtensions();

        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("   @color_dark_red___  @color_red__________   "));
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("  @color_dark_red/ _ \\@color_red/_  __/ __/ @color_dark_gray Disabled PowerToolS v" + PlInfo.VERSION));
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor(" @color_dark_red/ ___/ @color_red/ / _\\ \\   @color_dark_gray "));
        getServer().getConsoleSender().sendMessage(ColorFormat.formatColor("@color_dark_red/_/    @color_red/_/ /___/  @color_dark_gray  by ItzOver"));
        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage("");
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

    public enum PluginAddOns{
        Bedwars("  AntiTeamup:\n" +
                "    enabled: false\n" +
                "    distance: 5\n" +
                "    maxVL: 20\n" +
                "\n" +
                "  TntKnockback:\n" +
                "    enabled: false\n" +
                "    multiply: 1.6\n" +
                "\n" +
                "  FireballKnockback:\n" +
                "    enabled: false\n" +
                "    multiply: 1.4"),
        Hub("  KnockbackPlates:\n" +
                "    enabled: false\n" +
                "    locations:\n" +
                "      001:\n" +
                "        world: \"World\" # Insert world's NAME\n" +
                "        location: [100, 100] #NOTE: [X, Z]\n" +
                "        mode: \"HEAD\" # Insert \"HEAD\" to knockback to head direction or \"FORCE\" to knockback to a default location\n" +
                "        knockback: [1, 2, 1] # this only work when on FORCE\n" +
                "        multiply: 2 # this only work when on HEAD\n" +
                "        verticalNormal: 2\n" +
                "  VoidTP:\n" +
                "    enabled: false\n" +
                "    respawnLocation:\n" +
                "      x: 100\n" +
                "      y: 100\n" +
                "      z: 100\n" +
                "    teleportY: -5 # When player's Y position get lower than this value will teleport to the respawnLocation"),
        Survival("  NoRespawn: # BETA #When player kill by another player a head will drop from dead player! If that head place, player will respawn... else they're spectator\n" +
                "    enabled: false\n" +
                "\n" +
                "  ChatGame:\n" +
                "    # Delays are in InGameTicks: 20 ticks = 1 second: 1200 ticks = 1 minute\n" +
                "    enabled: false\n" +
                "    delay: 18000 # = every 15 mins\n" +
                "    rewardsPerGame: 1 # AI only select 1 reward per game\n" +
                "    answerTime: 1200 # 1 min insert -1 for disable\n" +
                "    rewards:\n" +
                "      Money: 10000 # 10000$ REQUIRED *VAULT ECONOMY*\n" +
                "      IRON_BLOCK: 1\n" +
                "      GOLD_BLOCK: 1\n" +
                "      EMERALD_BLOCK: 1\n" +
                "      DIAMOND: 3"),
        Server("  randomMOTD:\n" +
                "    enabled: false\n" +
                "    MOTDs:\n" +
                "      - \"&6THIS IS MY SERVER!\"\n" +
                "      - \"&6ANOTHER &5SPIGOT SERVER\"\n" +
                "\n" +
                "  ForcePing:\n" +
                "    enabled: false\n" +
                "    maxDelay: 5000\n" +
                "\n" +
                "  AntiRejoin: # Works better if ForcePing enabled\n" +
                "    enabled: false\n" +
                "    wait: 5000\n" +
                "\n" +
                "  BanMOTD:\n" +
                "    enabled: false\n" +
                "    MOTD: \"&cYou're Banned from mc.example.net\""),
        Render("  ScoreBoards:\n" +
                "    enabled: false\n" +
                "    switchDelay: 20 # 20 ticks = 1 second\n" +
                "    name: \"Minecraft server\"\n" +
                "    boards:\n" +
                "      index01:\n" +
                "        - \"&2Welcome to minecraft server\"\n" +
                "        - \"&2On %onlines-size%/%max-onlines-size%\"\n" +
                "        - \"&2You're using %cl-brand%\"\n" +
                "      index02:\n" +
                "        - \"&2Welcome to minecraft server\"\n" +
                "        - \"&2On %onlines-size%/%max-onlines-size%\"\n" +
                "        - \"&2You're using %cl-version%\"\n" +
                "    # You can insert more items than these");

        String config;


        PluginAddOns(String config){
            this.config = config;
        }

        void insert() throws FileNotFoundException, UnsupportedEncodingException {
            PrintWriter file = new PrintWriter("plugins\\PowerToolS\\config.yml", "UTF-8");
            file.println();
            for(int line = 0; line < config.split("\n").length; line++)
                file.println(config.split("\n")[line]);
            file.close();
        }

        void removeInserts() throws FileNotFoundException, UnsupportedEncodingException {
            PrintWriter file = new PrintWriter("plugins\\PowerToolS\\config.yml", "UTF-8");
            List<String> content = getConfigContent();
            for (String s : content)
                if (!Arrays.asList(config.split("\n")).contains(s))
                    file.println(s);
            file.close();
        }

        List<String> getConfigContent() throws FileNotFoundException {
            List<String> content = new ArrayList<>();
            Scanner sc = new Scanner(new File("plugins\\PowerToolS\\config.yml"));
            while(sc.hasNextLine()) content.add(sc.nextLine());
            return content;
        }
    }
}
