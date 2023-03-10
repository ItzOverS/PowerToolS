package me.overlight.powertools.spigot.Libraries;

public class ColorFormat {
    public static String formatColor(String msg) {
        String ms = msg;
        // Chat Colors
        ms = ms.replace("@color_black", "&0").replace("@color_dark_blue", "&1")
                .replace("@color_dark_green", "&2").replace("@color_dark_aqua", "&3")
                .replace("@color_dark_red", "&4").replace("@color_dark_purple", "&5")
                .replace("@color_gold", "&6").replace("@color_gray", "&7")
                .replace("@color_dark_gray", "&8").replace("@color_blue", "&9")
                .replace("@color_green", "&a").replace("@color_aqua", "&b")
                .replace("@color_red", "&c").replace("@color_light_purple", "&d")
                .replace("@color_yellow", "&e").replace("@color_white", "&f");
        // Chat Formats
        ms = ms.replace("@format_magic", "&k").replace("@format_bold", "&l")
                .replace("@format_mid_line", "&m").replace("@format_under_line", "&n")
                .replace("@format_italic", "&o").replace("@format_reset", "&r");
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', ms);
    }
    public static String formatColorBungee(String msg) {
        String ms = msg;
        // Chat Colors
        ms = ms.replace("@color_black", "&0").replace("@color_dark_blue", "&1")
                .replace("@color_dark_green", "&2").replace("@color_dark_aqua", "&3")
                .replace("@color_dark_red", "&4").replace("@color_dark_purple", "&5")
                .replace("@color_gold", "&6").replace("@color_gray", "&7")
                .replace("@color_dark_gray", "&8").replace("@color_blue", "&9")
                .replace("@color_green", "&a").replace("@color_aqua", "&b")
                .replace("@color_red", "&c").replace("@color_light_purple", "&d")
                .replace("@color_yellow", "&e").replace("@color_white", "&f");
        // Chat Formats
        ms = ms.replace("@format_magic", "&k").replace("@format_bold", "&l")
                .replace("@format_mid_line", "&m").replace("@format_under_line", "&n")
                .replace("@format_italic", "&o").replace("@format_reset", "&r");
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', ms);
    }

    public static String removeAllFormats(String text) {
        int index = 0;
        for (char ch : text.toCharArray()) {
            if (ch == '&') {
                text = text.substring(index, 2);
                index -= 2;
            }
            index++;
        }
        return text;
    }
}
