package me.overlight.powertools.discordlink;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import java.util.logging.LogRecord;

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
        if(s.contains("JDA")) return getOnMismatch();
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
        if(logEvent.getMessage().getFormattedMessage().contains("JDA")) return getOnMatch();
        return getOnMismatch();
    }
}
