package me.overlight.powertools.bukkit.Libraries;

public enum PremiumField {
    TRUE("YES"),
    FALSE("NO"),
    NONE("N/A");

    private final String f;

    PremiumField(String v) {
        f = v;
    }

    public String get() {
        return f;
    }
}
