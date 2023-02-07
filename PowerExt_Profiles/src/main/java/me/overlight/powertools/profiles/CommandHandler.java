package me.overlight.powertools.profiles;

import me.overlight.powertools.spigot.APIs.NetworkChecker;
import me.overlight.powertools.spigot.Libraries.ColorFormat;
import me.overlight.powertools.spigot.Modules.mods.CpsMap;
import me.overlight.powertools.spigot.Plugin.PlInfo;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler
        implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // -> /profile {player}
        if (args.length != 1) return false;
        if (Bukkit.getPlayer(args[0]) == null) return false;
        Player player = Bukkit.getPlayer(args[0]);
        String[] messages = {
                PlInfo.INV_PREFIX + "@color_goldName@color_gray: @color_red%USER_NAME%",
                //PlInfo.INV_PREFIX + "@color_goldUUID@color_gray: @color_red%USER_UUID%",
                PlInfo.INV_PREFIX + "@color_goldHover@color_red for more details"
        };
        String hover = "" +
                "@color_red|Display \n" +
                "  - @color_redName@color_gray: @color_red%USER_NAME% \n" +
                "  - @color_redDisplay Name@color_gray: @color_red%USER_DISPLAY_NAME% \n" +
                "  - @color_redUUID@color_gray: @color_red%USER_UUID% \n" +
                "  - @color_redIs Primium@color_gray: @color_red%IS_PREMIUM% \n" +
                "@color_red|CPS \n" +
                "  - @color_redMax CPS@color_gray: @color_red%USER_MAX_CPS% \n" +
                //"  - @color_redAverage CPS@color_gray: @color_red%USER_AVERAGE_CPS% \n" +
                "  - @color_redCurrent CPS@color_gray: @color_red%USER_CURRENT_CPS% \n" +
                "@color_red|NetWork \n" +
                "  - @color_redIPv4@color_gray: @color_red%USER_IP% \n" +
                "  - @color_redLocation@color_gray: @color_red%USER_COUNTRY% - %USER_CITY% \n";

        for (String message : messages) {
            TextComponent compo = new TextComponent(replaceArgs((Player) sender, ColorFormat.formatColor(message)));
            compo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorFormat.formatColor(replaceArgs((Player) sender, hover))).create()));
            player.spigot().sendMessage(compo);
        }
        return true;
    }

    public String replaceArgs(Player player, String str) {
        PlayerProfile pl = ProfileManager.getProfile(player);
        return str.replace("%USER_NAME%", pl.name())
                .replace("%USER_DISPLAY_NAME%", pl.displayName())
                .replace("%USER_UUID%", pl.uniqueID().toString())
                .replace("%USER_MAX_CPS%", CpsMap.MaxLMB.getOrDefault(player.getName(), 0) + "@color_gray | @color_red" + CpsMap.MaxRMB.getOrDefault(player.getName(), 0))
                .replace("%USER_CURRENT_CPS%", CpsMap.LMB.getOrDefault(player.getName(), 0) + "@color_gray | @color_red" + CpsMap.RMB.getOrDefault(player.getName(), 0))
                .replace("%USER_IP%", NetworkChecker.getPlayerIPv4(player) == "" ? "N/A" : NetworkChecker.getPlayerIPv4(player))
                .replace("%USER_CITY%", NetworkChecker.getPlayerCity(player) == null ? "N/A" : NetworkChecker.getPlayerCity(player))
                .replace("%USER_COUNTRY%", NetworkChecker.getPlayerCountry(player) == null ? "N/A" : NetworkChecker.getPlayerCountry(player));
    }
}
