package me.overlight.powertools.spigot.Modules.mods;

import me.overlight.powertools.spigot.Libraries.DateTime;
import me.overlight.powertools.spigot.Libraries.MuteEntry;
import me.overlight.powertools.spigot.Modules.Module;
import me.overlight.powertools.spigot.Plugin.PlMessages;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mute
        extends Module
        implements Listener {
    public Mute() {
        super("Mute", "Mute player to preventing from them to send message in chat or sign", "PowerToolS {Mute/UnMute} {target}", new String[]{});
        new BukkitRunnable() {
            @Override
            public void run() {
                for (MuteEntry m : mutedPlayers) {
                    if (currentTime().isBiggerThan(pasteAs(m.getUntil()).plusBy(m.getDate())))
                        mutedPlayers.remove(m);
                }
            }
        }.runTaskTimer(PowerTools.INSTANCE, 0, 15);
    }

    public static List<MuteEntry> mutedPlayers = new ArrayList<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(AsyncPlayerChatEvent e) {
        for (MuteEntry m : mutedPlayers) {
            if (Objects.equals(m.getName(), e.getPlayer().getName())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(PlMessages.Mute_YouCantSendChatMessage.get());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getState().getType().name().toLowerCase().contains("sign")) {
            for (MuteEntry m : mutedPlayers) {
                if (m.getName().equals(e.getPlayer().getName())) {
                    for (Player pl : e.getPlayer().getWorld().getPlayers()) {
                        if (pl == e.getPlayer()) continue;
                        if (pl.getLocation().distance(e.getPlayer().getLocation()) < 7) {
                            e.setCancelled(true);
                            e.getPlayer().sendMessage(PlMessages.Mute_YouCantPlaceSign.get());
                        }
                    }
                }
            }
        }
    }

    private DateTime pasteAs(String code) {
        code = code.toLowerCase();
        if (code.contains("s"))
            return new DateTime(0, 0, 0, 0, 0, Integer.parseInt(code.replace("s", "")));
        else if (code.contains("mo"))
            return new DateTime(0, Integer.parseInt(code.replace("mo", "")), 0, 0, 0, 0);
        else if (code.contains("m"))
            return new DateTime(0, 0, 0, 0, Integer.parseInt(code.replace("m", "")), 0);
        else if (code.contains("h"))
            return new DateTime(0, 0, 0, Integer.parseInt(code.replace("h", "")), 0, 0);
        else if (code.contains("d"))
            return new DateTime(0, 0, Integer.parseInt(code.replace("d", "")), 0, 0, 0);
        else if (code.contains("w"))
            return new DateTime(0, 0, Integer.parseInt(code.replace("w", "")) * 7, 0, 0, 0);
        else if (code.contains("y"))
            return new DateTime(Integer.parseInt(code.replace("y", "")), 0, 0, 0, 0, 0);
        return null;
    }

    public static DateTime currentTime() {
        LocalDateTime m = LocalDateTime.now();
        return new DateTime(
                m.getYear(),
                m.getMonthValue(),
                m.getDayOfMonth(),
                m.getHour(),
                m.getMinute(),
                m.getSecond()
        );
    }

    public static boolean isPlayerMuted(String username) {
        for (MuteEntry m : mutedPlayers) {
            if (m.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void unMute(String username) {
        mutedPlayers.removeIf(m -> m.getName().equals(username));
    }
}
