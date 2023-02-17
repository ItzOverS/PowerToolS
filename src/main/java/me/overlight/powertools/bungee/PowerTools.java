package me.overlight.powertools.bungee;

import me.overlight.powertools.bungee.AddOns.AntiProtocolDetect;
import me.overlight.powertools.spigot.Libraries.ColorFormat;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public final class PowerTools
        extends Plugin {
    public static PowerTools INSTANCE;
    public static Configuration config;

    @Override
    public void onEnable() {
        try {
            INSTANCE = this;
            getProxy().registerChannel("pts:bungee");
            getProxy().getPluginManager().registerListener(this, new MainEventHandler());

            saveResource("bungeeConfig.yml", false);
            config = loadResource(getDataFolder().getPath() + "\\config.yml");

            if (config.getBoolean("AntiProtocolDetect.enabled"))
                getProxy().getPluginManager().registerListener(this, new AntiProtocolDetect());

            getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("   @color_dark_green___  @color_aqua__________   ")).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("  @color_dark_green/ _ \\@color_aqua/_  __/ __/ @color_dark_gray Welcome to PowerToolS v" + PlInfo.VERSION)).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee(" @color_dark_green/ ___/ @color_aqua/ / _\\ \\   @color_dark_gray Running on BungeeCord")).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("@color_dark_green/_/    @color_aqua/_/ /___/  @color_dark_gray  by ItzOver")).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
            getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
        } catch (Exception ex) {
            getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("   @color_dark_red___  @color_red__________   ")).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("  @color_dark_red/ _ \\@color_red/_  __/ __/ @color_dark_gray " + ex.getLocalizedMessage())).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee(" @color_dark_red/ ___/ @color_red/ / _\\ \\   @color_dark_gray Disabled PowerToolS v" + PlInfo.VERSION)).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("@color_dark_red/_/    @color_red/_/ /___/  @color_dark_gray  by ItzOver")).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
            getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
        }
    }

    @Override
    public void onDisable() {
        getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
        getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("   @color_dark_red___  @color_red__________   ")).create());
        getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("  @color_dark_red/ _ \\@color_red/_  __/ __/ @color_dark_gray ")).create());
        getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee(" @color_dark_red/ ___/ @color_red/ / _\\ \\   @color_dark_gray Disabled PowerToolS v" + PlInfo.VERSION)).create());
        getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("@color_dark_red/_/    @color_red/_/ /___/  @color_dark_gray  by ItzOver")).create());
        getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
        getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
    }

    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            if (new File(getDataFolder(), "config.yml").exists()) return;
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = this.getResourceAsStream(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in ");
            } else {
                File outFile = new File(getDataFolder(), "config.yml");
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (outFile.exists() && !replace) {
                    } else {
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException var10) {
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

    public Configuration loadResource(String path) throws IOException {
        return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(path));
    }
}
