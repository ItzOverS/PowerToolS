package me.overlight.powertools.discordlink;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class JDAMessageDeny
        implements Filter {

    @Override
    public Result getOnMismatch() {
        return Result.DENY;
    }

    @Override
    public Result getOnMatch() {
        return Result.ACCEPT;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object... objects) {
        if (s.contains("JDA")) return getOnMismatch();
        return getOnMatch();
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object o, Throwable throwable) {
        return getOnMatch();
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return getOnMatch();
    }

    @Override
    public Result filter(LogEvent logEvent) {
        if (logEvent.getMessage().getFormattedMessage().contains("JDA")) return getOnMatch();
        return getOnMismatch();
    }
}
