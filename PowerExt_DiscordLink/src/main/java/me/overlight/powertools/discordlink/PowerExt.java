package me.overlight.powertools.discordlink;

import me.overlight.powertools.Libraries.PluginYaml;
import me.overlight.powertools.Plugin.PlInfo;
import me.overlight.powertools.PowerModules.PowerModule;
import me.overlight.powertools.PowerTools;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;

public final class PowerExt
        extends JavaPlugin
        implements PowerModule {
    public static JDA bot;
    public static String dsPrefix = "$$";
    public static HashMap<String, String> playerCodes = new HashMap<>();
    public static HashMap<String, String> discordIDsUser = new HashMap<>();
    public static PowerModule module;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            YamlConfiguration yml = new PluginYaml("discordLinks").getYaml();
            for (String key : yml.getKeys(false)) {
                discordIDsUser.put(key, yml.getString(key));
            }
        } catch (IOException ignored) {
            getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.RED + "Failed to create discordLinks file");
        }
        module = this;
        try {
            //((Logger) LogManager.getRootLogger()).addFilter((Filter)new JDAMessageDeny());
            bot = JDABuilder.createDefault(PowerTools.config.getString(getConfigName() + ".token"), GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES).build();
            bot.addEventListener(new DiscordMessageListener(this));
            getServer().getPluginManager().registerEvents(new GameEventHandler(), this);
        } catch (Exception ex) {
            PowerTools.INSTANCE.getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.RED + "Failed to log-in to discord bot: " + ex.getMessage());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public String[] getConfiguration() {
        return new String[]{
                "enabled:false:boolean",
                "token:'YOUR_TOKEN':string",
                "channelID:YOUR_VERIFY_CHANNEL_ID:string"
        };
    }

    @Override
    public String getConfigName() {
        return "DiscordLink";
    }

    @Override
    public String getExtensionPrefix() {
        return ChatColor.RED + "Discord" + ChatColor.AQUA + "Link";
    }

    @Override
    public PowerModule getPowerModule() {
        return this;
    }
}
