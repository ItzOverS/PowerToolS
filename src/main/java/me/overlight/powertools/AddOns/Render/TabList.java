package me.overlight.powertools.AddOns.Render;

import me.clip.placeholderapi.PlaceholderAPI;
import me.overlight.powertools.AddOns.AddOn;
import me.overlight.powertools.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TabList
        extends AddOn {
    public TabList() {
        super("RenderAddOns.TabList", "1.0", "Make your tab costume", "NONE", PowerTools.config.getBoolean("RenderAddOns.TabList"));
    }

    private static int index = 0;

    @Override
    public void onEnabled() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PowerTools.INSTANCE, () -> {
            ConfigurationSection sec =
                    PowerTools.config.getConfigurationSection(this.getName() + ".lists." + new ArrayList<>(PowerTools.config.getConfigurationSection(this.getName() + ".lists").getKeys(false)).get(index));
            String headerString = ChatColor.translateAlternateColorCodes('&', mixArray(sec.getStringList("header"), "\n")),
                    footerString = ChatColor.translateAlternateColorCodes('&', mixArray(sec.getStringList("footer"), "\n"));
            for(Player player: Bukkit.getOnlinePlayers()) {
                player.setPlayerListName(PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', sec.getString("per-player"))));
                net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter packet = new net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter();
                Object header = new net.minecraft.server.v1_8_R3.ChatComponentText(PlaceholderAPI.setPlaceholders(player, headerString)),
                        footer = new net.minecraft.server.v1_8_R3.ChatComponentText(PlaceholderAPI.setPlaceholders(player, footerString));
                try {
                    Field a = packet.getClass().getDeclaredField("a"),
                            b = packet.getClass().getDeclaredField("b");
                    a.setAccessible(true);
                    b.setAccessible(true);
                    a.set(packet, header);
                    b.set(packet, footer);
                    ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                }
            }
            index++;
        }, 0, PowerTools.config.getInt(this.getName() + ".switchDelay"));
    }

    private String mixArray(List<String> array, String splitter){
        String main = "";
        for(String s: array)
            main += s + splitter;
        return main.substring(0, main.length() - splitter.length());
    }
}
