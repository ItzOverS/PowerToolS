package me.overlight.powertools.Modules.impls;

import me.overlight.powertools.NMSSupport;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class AlertUtils {
    public static void sendActionBar(Player player, String text) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Object packet = Objects.requireNonNull(NMSSupport.getClass("PacketPlayOutChat")).getConstructor(NMSSupport.getClass("IChatBaseComponent"), byte.class).newInstance(Objects.requireNonNull(NMSSupport.getClass("ChatComponentText")).getConstructor(String.class).newInstance(text), (byte)2);
        NMSSupport.sendPacket(player, packet);
    }
}
