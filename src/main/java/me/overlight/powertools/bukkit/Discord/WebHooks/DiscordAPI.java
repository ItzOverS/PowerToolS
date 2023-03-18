package me.overlight.powertools.bukkit.Discord.WebHooks;

import me.overlight.powertools.bukkit.Plugin.PlInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DiscordAPI {

    public static String DiscordWebHookURL;

    public static void sendEmbedOnWebhook(String title, String desc) {
        if (DiscordWebHookURL != null) {
            DiscordWebhook webhook = new DiscordWebhook(DiscordWebHookURL);
            DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
            embed.setAuthor("PowerToolS", null, "https://s6.uupload.ir/files/icon_zbxl.png");
            embed.setTitle(title);
            embed.setDescription(desc);
            embed.setColor(java.awt.Color.YELLOW);
            webhook.addEmbed(embed);
            try {
                webhook.execute();
            } catch (Exception ex) {
                Bukkit.getServer().getConsoleSender().sendMessage(PlInfo.PREFIX + ChatColor.RED + "Something went wrong: " + ex.getMessage());
            }
        }
    }
}
