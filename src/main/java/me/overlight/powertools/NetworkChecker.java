package me.overlight.powertools;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkChecker {
    public static String getPlayerCountry(Player player) {
        return (String) getField(player, "country");
    }
    public static String getPlayerISP(Player player){
        return (String) getField(player, "isp");
    }
    public static String getPlayerCity(Player player){
        return (String) getField(player, "city");
    }
    public static String getPlayerContinent(Player player){
        return (String) getField(player, "continent");
    }
    public static Boolean isPlayerProxy(Player player){
        return (Boolean) getField(player, "proxy");
    }

    public static Object getField(Player player, String field){
        JSONObject json = getPlayerIPv4API(player);
        if(json == null) return null;
        return json.get(field);
    }

    public static String getPlayerIPv4(Player player){
        return player.getAddress().getAddress().toString().split("/")[0];
    }

    public static JSONObject getPlayerIPv4API(Player player){
        try {
            HttpURLConnection client = (HttpURLConnection) new URL("http://ip-api.com/json/" + getPlayerIPv4(player) + "?fields=26894865").openConnection();
            client.setRequestMethod("GET");
            client.setRequestProperty("accept", "application/json");
            client.setRequestProperty("userAgent", "Mozilla/5.0");
            if(client.getResponseCode() != 200){
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
        } in.close();
        JSONParser json = new JSONParser();
        return (JSONObject)(json.parse(response.toString()));
    }
}
