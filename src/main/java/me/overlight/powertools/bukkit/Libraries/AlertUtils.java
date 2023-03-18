package me.overlight.powertools.bukkit.Libraries;

import me.overlight.powertools.bukkit.APIs.NMSSupport;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class AlertUtils {
    public static void sendActionBar(Player player, String text) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Object packet = Objects.requireNonNull(NMSSupport.getMinecraftServerClass("PacketPlayOutChat")).getConstructor(NMSSupport.getMinecraftServerClass("IChatBaseComponent"), byte.class).newInstance(Objects.requireNonNull(NMSSupport.getMinecraftServerClass("ChatComponentText")).getConstructor(String.class).newInstance(text), (byte) 2);
        NMSSupport.sendPacket(player, packet);
    }

    public static void sendHoverClickableMessage(Player player, String content, ClickEvent clickEvent, HoverEvent hoverEvent) {
        TextComponent compo = new TextComponent(content);
        compo.setClickEvent(clickEvent);
        compo.setHoverEvent(hoverEvent);
        player.spigot().sendMessage(compo);
    }
}
