package me.overlight.powertools.bukkit.AddOns.Main;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.Libraries.ColorFormat;
import me.overlight.powertools.bukkit.Libraries.RepItem;
import me.overlight.powertools.bukkit.Plugin.PlMessages;
import me.overlight.powertools.bukkit.PowerTools;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NetworkChecker
        extends AddOn
        implements Listener {
    public NetworkChecker() {
        super("NetworkChecker", "1.0", "Check players ip", PowerTools.config.getBoolean("NetworkChecker.enabled"));
        me.overlight.powertools.bukkit.APIs.NetworkChecker.runRequestChecks();
    }

    @EventHandler
    public void event(PlayerJoinEvent e) {
        TextComponent compo = new TextComponent(PlMessages.NetworkChecker_PlayerJoinedUsing.get(new RepItem("%PLAYER_NAME%", e.getPlayer().getName()), new RepItem("%IP%", me.overlight.powertools.bukkit.APIs.NetworkChecker.getPlayerIPv4(e.getPlayer()))));
        String hover = ColorFormat.formatColor(
                "@color_redUserName@color_gray: @color_red" + e.getPlayer().getName() + "\n" +
                        "@color_redIP@color_gray: @color_red" + me.overlight.powertools.bukkit.APIs.NetworkChecker.getPlayerIPv4(e.getPlayer()) + "\n" +
                        "@color_redCountry@color_gray: @color_red" + me.overlight.powertools.bukkit.APIs.NetworkChecker.getPlayerCountry(e.getPlayer()) + "\n" +
                        "@color_redCity@color_gray: @color_red" + me.overlight.powertools.bukkit.APIs.NetworkChecker.getPlayerCity(e.getPlayer()) + "\n" +
                        "@color_redVpn@color_gray: @color_red" + me.overlight.powertools.bukkit.APIs.NetworkChecker.isPlayerProxy(e.getPlayer()) + "\n"
        );
        compo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        for (OfflinePlayer player : Bukkit.getOperators()) {
            if (!player.isOnline()) continue;
                Bukkit.getPlayer(player.getName()).spigot().sendMessage(compo);
        }
        {
            String IPv4 = me.overlight.powertools.bukkit.APIs.NetworkChecker.getPlayerIPv4(e.getPlayer());
            if (PowerTools.config.getStringList(this.getName() + ".blackListedIPs").contains(IPv4)) {
                e.getPlayer().setBanned(true);
            }
        }
        {
            boolean isWhiteList = PowerTools.config.getString(this.getName() + ".CountriesList.type").equals("whitelist");
            if (isWhiteList) {
                String country = (String) me.overlight.powertools.bukkit.APIs.NetworkChecker.getField(e.getPlayer(), "countryCode");
                if (country == null) return;
                if (!PowerTools.config.getStringList(this.getName() + ".CountriesList.list").contains(country)) {
                    e.getPlayer().setBanned(true);
                    Bukkit.banIP(me.overlight.powertools.bukkit.APIs.NetworkChecker.getPlayerIPv4(e.getPlayer()));
                }
                for (OfflinePlayer player : Bukkit.getOperators()) {
                    if (!player.isOnline()) continue;
                    Bukkit.getPlayer(player.getName()).sendMessage(PlMessages.NetworkChecker_PlayerTempBannedForInvalidCountry.get(new RepItem("%PLAYER_NAME%", e.getPlayer().getName())));
                }
            } else {
                String country = (String) me.overlight.powertools.bukkit.APIs.NetworkChecker.getField(e.getPlayer(), "countryCode");
                if (country == null) return;
                if (PowerTools.config.getStringList(this.getName() + ".CountriesList.list").contains(country)) {
                    e.getPlayer().setBanned(true);
                    Bukkit.banIP(me.overlight.powertools.bukkit.APIs.NetworkChecker.getPlayerIPv4(e.getPlayer()));
                }
                for (OfflinePlayer player : Bukkit.getOperators()) {
                    if (!player.isOnline()) continue;
                    Bukkit.getPlayer(player.getName()).sendMessage(PlMessages.NetworkChecker_PlayerTempBannedForInvalidCountry.get(new RepItem("%PLAYER_NAME%", e.getPlayer().getName())));
                }
            }
        }
    }
}
