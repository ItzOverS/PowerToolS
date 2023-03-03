package me.overlight.powertools.spigot;

import me.overlight.powertools.spigot.Libraries.ColorFormat;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class ConsoleMessageFilter
        implements Filter {

    String[] denyMsgList = {
            "[PowerToolS] Disabling PowerToolS",
            "[PlaceholderAPI] Successfully registered expansion: PowerToolS",
            "[PowerToolS] Enabling PowerToolS",
            "[PowerToolS] Loading PowerToolS",
            "handleDisconnection() called twice",
            " logged in with entity id ",
            "lost connection",
            "left the game",
            "UUID of player"
    };

    public Filter.Result checkMessage(String message) {
        for (String m : denyMsgList) {
            if (message.contains(m)) {
                return Result.DENY;
            }
        }
        ConsoleMessageSaver.addConsoleMessage(ColorFormat.removeAllFormats(message));
        return Result.NEUTRAL;
    }

    public boolean isStarted() {
        return true;
    }

    public boolean isStopped() {
        return false;
    }

    public void start() {
    }

    public void stop() {
    }

    @Override
    public Result getOnMismatch() {
        return null;
    }

    @Override
    public Result getOnMatch() {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object... objects) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object o, Throwable throwable) {
        return checkMessage(String.valueOf(o));
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return checkMessage(message.getFormattedMessage());
    }

    @Override
    public Result filter(LogEvent logEvent) {
        return checkMessage(logEvent.getMessage().getFormattedMessage());
    }
}
