package me.overlight.powertools.Libraries;

public class RepItem {
    private String key, value;
    public RepItem(String key, String value){
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
