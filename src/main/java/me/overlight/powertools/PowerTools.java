package me.overlight.powertools;

import com.avaje.ebean.SqlRow;
import com.avaje.ebeaninternal.server.cluster.Packet;
import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.powertools.Command.MainCommand;
import me.overlight.powertools.Libraries.WebHooks.DiscordAPI;
import me.overlight.powertools.Libraries.WebHooks.DiscordWebhook;
import me.overlight.powertools.Modules.ModuleManager;
import me.overlight.powertools.Modules.mods.*;
import me.overlight.powertools.Plugin.PlInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PowerTools
        extends JavaPlugin {
    public static PowerTools INSTANCE;
    FileConfiguration config;
    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEvents.get().getSettings()
                .checkForUpdates(true)
                .bStats(true)
                .fallbackServerVersion(PacketEvents.get().getServerUtils().getVersion());
        PacketEvents.get().load();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
        config = getConfig();

        getServer().getPluginCommand("powertools").setExecutor(new MainCommand());
        getServer().getPluginCommand("powertools").setTabCompleter(new MainCommand());

        discordWebhookConnection:
        {
            if (config.getBoolean("discordWebhook.enabled")) {
                String url = config.getString("discordWebhook.url");
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

        ModuleManager.registerModule(new Knockback(), new Freeze(), new Channel(), new MemoryUsage(), new Protect(), new Rotate(), new PlayTime());
        ModuleManager.loadModulesData();
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }

    public enum Target{
        STAFF,
        MEMBERS
    }
    public static void Alert(Target targ, String msg){
        for(Player player: Bukkit.getOnlinePlayers()){
            if(targ == Target.STAFF) {
                if (player.isOp())
                    player.sendMessage(PlInfo.PREFIX + msg);
            } else {
                player.sendMessage(PlInfo.PREFIX + msg);
            }
        }
    }
}
