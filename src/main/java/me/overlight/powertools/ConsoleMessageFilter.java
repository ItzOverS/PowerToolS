package me.overlight.powertools;

import me.overlight.powertools.Libraries.PluginYaml;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConsoleMessageFilter
        implements Filter {

    String[] denyMsgList = {
            "[PowerToolS] Disabling PowerToolS",
            "[PlaceholderAPI] Successfully registered expansion: PowerToolS",
            "[PowerToolS] Enabling PowerToolS",
            "[PowerToolS] Loading PowerToolS"
    };

    public Filter.Result checkMessage(String message) {
        for(String m: denyMsgList){
            if(message.contains(m)){
                return Result.DENY;
            }
        }
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
