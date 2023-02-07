package me.overlight.powertools.AddOns.Render;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.NMSSupport;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TabList
        extends AddOn {
    public TabList() {
        super("RenderAddOns.TabList", "1.0", "Make your tab costume", PowerTools.config.getBoolean("RenderAddOns.TabList.enabled"));
    }

    private static int index = 0;

    @Override
    public void onEnabled() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PowerTools.INSTANCE, () -> {
            ConfigurationSection sec =
                    PowerTools.config.getConfigurationSection(this.getName() + ".lists." + new ArrayList<>(PowerTools.config.getConfigurationSection(this.getName() + ".lists").getKeys(false)).get(index));
            String headerString = ChatColor.translateAlternateColorCodes('&', mixArray(sec.getStringList("header"), "\n")),
                    footerString = ChatColor.translateAlternateColorCodes('&', mixArray(sec.getStringList("footer"), "\n"));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                    player.setPlayerListName(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', sec.getString("per-player"))));
                else
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', sec.getString("per-player")));
                try {
                    Object packet = Objects.requireNonNull(NMSSupport.getClass("PacketPlayOutPlayerListHeaderFooter")).getConstructor().newInstance();
                    Object header = Objects.requireNonNull(NMSSupport.getClass("ChatComponentText")).getConstructor(String.class).newInstance((Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) ? me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, headerString) : headerString),
                            footer = Objects.requireNonNull(NMSSupport.getClass("ChatComponentText")).getConstructor(String.class).newInstance((Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) ? me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, footerString) : footerString);
                    Field a = packet.getClass().getDeclaredField((PacketEvents.get().getServerUtils().getVersion().isOlderThan(ServerVersion.v_1_13)) ? "a" : "header"),
                            b = packet.getClass().getDeclaredField((PacketEvents.get().getServerUtils().getVersion().isOlderThan(ServerVersion.v_1_13)) ? "b" : "footer");
                    a.setAccessible(true);
                    b.setAccessible(true);
                    a.set(packet, header);
                    b.set(packet, footer);

                    NMSSupport.sendPacket(player, packet);
                } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException |
                         InvocationTargetException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
            index++;
            if (index >= PowerTools.config.getConfigurationSection(this.getName() + ".lists").getKeys(false).size())
                index = 0;
        }, 0, PowerTools.config.getInt(this.getName() + ".switchDelay"));
    }

    private String mixArray(List<String> array, String splitter) {
        String main = "";
        for (String s : array)
            main += s + splitter;
        return main.substring(0, main.length() - splitter.length());
    }
}
