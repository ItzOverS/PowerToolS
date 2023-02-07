package me.overlight.powertools.APIs;

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
