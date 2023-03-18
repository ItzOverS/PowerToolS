package me.overlight.powertools.bukkit.AddOns.Server;

import me.overlight.powertools.bukkit.AddOns.AddOn;
import me.overlight.powertools.bukkit.PowerTools;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class ConsoleMessageDeny
        extends AddOn
        implements Filter {
    public ConsoleMessageDeny() {
        super("ServerAddOns.ConsoleMessageDeny", "1.0", "Filter what messages you don't want to see in console", PowerTools.config.getBoolean("ServerAddOns.ConsoleMessageDeny.enabled"));
    }

    @Override
    public void onEnabled() {
        ((Logger) LogManager.getRootLogger()).addFilter(this);
    }

    public Result checkMessage(String message) {
        for (String v : PowerTools.config.getStringList(this.getName() + ".messages")) {
            if (message.contains(v)) {
                return getOnMatch();
            }
        }
        return getOnMismatch();
    }

    @Override
    public Result getOnMismatch() {
        return Result.NEUTRAL;
    }

    @Override
    public Result getOnMatch() {
        return Result.DENY;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object... objects) {
        return checkMessage(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object o, Throwable throwable) {
        return checkMessage((String) o);
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
