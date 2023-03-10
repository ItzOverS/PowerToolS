package me.overlight.powertools.spigot.APIs;

import me.overlight.powertools.spigot.Libraries.PremiumField;
import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.time.LocalDateTime;

public class NetworkChecker {
    private static Player p;
    private static JSONObject js;
    private static int requests = 0;

    public static String getPlayerCountry(Player player) {
        return (String) getField(player, "country");
    }

    public static String getPlayerISP(Player player) {
        return (String) getField(player, "isp");
    }

    public static String getPlayerCity(Player player) {
        return (String) getField(player, "city");
    }

    public static String getPlayerContinent(Player player) {
        return (String) getField(player, "continent");
    }

    public static Boolean isPlayerProxy(Player player) {
        return (Boolean) getField(player, "proxy");
    }

    public static Object getField(Player player, String field) {
        JSONObject json;
        if (p != player) {
            json = getPlayerIPv4API(player);
            if (json == null) return null;
        } else {
            json = js;
        }
        if (json.get("status").equals("fail")) return null;
        p = player;
        js = json;
        return json.get(field);
    }

    public static PremiumField isPremium(Player player) {
        try {
            HttpURLConnection client = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + player.getName()).openConnection();
            client.setRequestMethod("GET");
            client.setRequestProperty("accept", "application/json");
            client.setRequestProperty("userAgent", "Mozilla/5.0");
            if (client.getResponseCode() != 200) return PremiumField.FALSE;
            return PremiumField.TRUE;
        } catch (Exception ex) {
            return PremiumField.FALSE;
        }
    }

    public static String getPlayerIPv4(Player player) {
        return player.getAddress().getAddress().toString().split("/")[0];
    }

    public static String getPlayerIPv4(InetSocketAddress address) {
        return address.getAddress().toString().split("/")[0];
    }

    public static String getPremiumPlayerUUID(Player player) throws IOException, ParseException {
        HttpURLConnection client = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + player.getName()).openConnection();
        client.setRequestMethod("GET");
        client.setRequestProperty("accept", "application/json");
        client.setRequestProperty("userAgent", "Mozilla/5.0");
        if (client.getResponseCode() != 200) return null;
        return (String) getAsJsoN(client.getInputStream()).get("uuid");
    }

    public static JSONObject getPlayerIPv4API(Player player) {
        try {
            if (requests > 43) throw new StackOverflowError("Requests excepted");
            HttpURLConnection client = (HttpURLConnection) new URL("http://ip-api.com/json/" + getPlayerIPv4(player) + "?fields=1196571").openConnection();
            client.setRequestMethod("GET");
            client.setRequestProperty("accept", "application/json");
            client.setRequestProperty("userAgent", "Mozilla/5.0");
            if (client.getResponseCode() != 200) {
                return null;
            }
            return getAsJsoN(client.getInputStream());
        } catch (Exception e) {
            return null;
        }
    }

    private static JSONObject getAsJsoN(InputStream c) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(new InputStreamReader(c));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONParser json = new JSONParser();
        return (JSONObject) (json.parse(response.toString()));
    }

    private static int currentMinute = -1;

    public static void runRequestChecks() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PowerTools.INSTANCE, () -> {
            if (currentMinute != LocalDateTime.now().toLocalTime().getMinute()) {
                requests = 0;
            }
            currentMinute = LocalDateTime.now().toLocalTime().getMinute();
        }, 5, 5);
    }
}
