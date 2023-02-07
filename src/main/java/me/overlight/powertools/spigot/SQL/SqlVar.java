package me.overlight.powertools.spigot.SQL;

public class SqlVar {
    private String key, value;

    public SqlVar(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getName() {
        return key;
    }

    public void setName(String key) {
        this.key = key;
    }

    public String getType() {
        return value;
    }

    public void setType(String value) {
        this.value = value;
    }

    public static String getAsSqlAction(String primary, SqlVar... hashes) {
        String result = "(";
        for (SqlVar hash : hashes) {
            result += hash.getName() + " " + hash.getType() + ",";
        }
        return result + "PRIMARY KEY (" + primary + "))";
    }
}
