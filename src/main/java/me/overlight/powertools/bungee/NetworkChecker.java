package me.overlight.powertools.bungee;

import net.md_5.bungee.api.connection.PendingConnection;

public class NetworkChecker {
    public static String getPlayerIPv4(PendingConnection connection) {
        return connection.getAddress().getAddress().toString();
    }
}
