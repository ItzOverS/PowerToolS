package me.overlight.powertools.bungee;

import me.overlight.powertools.spigot.Libraries.ColorFormat;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;

public final class PowerTools extends Plugin {
    public static PowerTools INSTANCE;
    @Override
    public void onEnable() {
        try {
            INSTANCE = this;
            getProxy().registerChannel("pts:bungee");
            getProxy().getPluginManager().registerListener(this, new PluginChannelListener());

            getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("   @color_dark_green___  @color_aqua__________   ")).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("  @color_dark_green/ _ \\@color_aqua/_  __/ __/ @color_dark_gray Welcome to PowerToolS v" + PlInfo.VERSION)).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee(" @color_dark_green/ ___/ @color_aqua/ / _\\ \\   @color_dark_gray Running on BungeeCord")).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColorBungee("@color_dark_green/_/    @color_aqua/_/ /___/  @color_dark_gray  by ItzOver")).create());
            getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
            getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
        } catch(Exception ex){

        }
    }

    @Override
    public void onDisable() {
        getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
        getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColor("   @color_dark_red___  @color_red__________   ")).create());
        getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColor("  @color_dark_red/ _ \\@color_red/_  __/ __/ @color_dark_gray ")).create());
        getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColor(" @color_dark_red/ ___/ @color_red/ / _\\ \\   @color_dark_gray Disabled PowerToolS v" + PlInfo.VERSION)).create());
        getProxy().getConsole().sendMessage(new ComponentBuilder(ColorFormat.formatColor("@color_dark_red/_/    @color_red/_/ /___/  @color_dark_gray  by ItzOver")).create());
        getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
        getProxy().getConsole().sendMessage(new ComponentBuilder("").create());
    }
}
