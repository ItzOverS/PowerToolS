package me.overlight.powertools.spigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class NMSSupport {
    public static Class<?> getClass(String className) {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + className);
        } catch (Exception e) {
            return null;
        }
    }

    public static void sendPacket(Player player, Object packet)
            throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Object handle = player.getClass().getMethod("getHandle").invoke(player),
                playerConnection = handle.getClass().getField("playerConnection").get(handle);
        playerConnection.getClass().getMethod("sendPacket", NMSSupport.getClass("Packet")).invoke(playerConnection, packet);
    }
}
