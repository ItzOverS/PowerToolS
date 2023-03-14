package me.overlight.powertools;

import org.bukkit.Bukkit;

import static io.github.retrooper.packetevents.utils.server.ServerVersion.getNMSSuffix;

public class ServerData {
    public static String getServerNMSVersion() {
        return getNMSSuffix();
    }

    public static String getServerVersion() {
        return Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf("-"));
    }

    public static boolean isNewerThan(String v1, String v2) {
        if (v1.split("\\.").length < v2.split("\\.").length)
            while (v1.split("\\.").length < v2.split("\\.").length) v1 += ".0";
        if (v1.split("\\.").length > v2.split("\\.").length)
            while (v1.split("\\.").length > v2.split("\\.").length) v2 += ".0";

        if (Integer.parseInt(v1.split("\\.")[0]) < Integer.parseInt(v2.split("\\.")[0])) {
            return false;
        } else if (Integer.parseInt(v1.split("\\.")[0]) == Integer.parseInt(v2.split("\\.")[0])) {
            return isNewerThan(v1.substring(v1.split("\\.")[0].length() + 1), v2.substring(v2.split("\\.")[0].length() + 1));
        } else {
            return true;
        }
    }

    public static ServerSoftware getServerSoftware() {
        String software = Bukkit.getVersion().substring(0, Bukkit.getVersion().indexOf("(")).toLowerCase();
        for (ServerSoftware soft : ServerSoftware.values()) {
            if (soft == ServerSoftware.ERR) continue;
            if (software.contains(soft.getIndexID())) return soft;
        }
        return ServerSoftware.ERR;
    }

    public enum ServerSoftware {
        //WorldHandling Servers
        Spigot("spigot"), Bukkit("bukkit"), Purpur("purpur"), Paper("paper"), Sponge("sponge"),

        //Proxy Servers
        BungeeCord("bungee"), WaterFall("waterfall"), Velocity("velocity"),

        //Not Found
        ERR(null);
        private final String indexName;

        ServerSoftware(String indexID) {
            indexName = indexID;
        }

        public String getIndexID() {
            return indexName;
        }
    }

    public static String formatVersion(String packetEventsVersion) {
        return packetEventsVersion.replace("v_", "").replace("_", ".");
    }
}
