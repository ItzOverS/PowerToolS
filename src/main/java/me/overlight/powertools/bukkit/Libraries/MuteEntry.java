package me.overlight.powertools.bukkit.Libraries;

public class MuteEntry {
    private String name, until, reason;
    private DateTime date;

    public String getName() {
        return name;
    }

    public String getUntil() {
        return until;
    }

    public String getReason() {
        return reason;
    }

    public DateTime getDate() {
        return date;
    }

    public MuteEntry(String name, String until, String reason, DateTime date) {
        this.name = name;
        this.until = until;
        this.reason = reason;
        this.date = date;
    }
}
