package me.overlight.powertools.bukkit.Libraries;

public class RepItem {
    private final String key;
    private final String value;

    public RepItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String key() {
        return key;
    }

    public String value() {
        return value;
    }
}
