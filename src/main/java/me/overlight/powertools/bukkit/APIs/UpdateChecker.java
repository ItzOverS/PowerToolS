package me.overlight.powertools.bukkit.APIs;

import me.overlight.powertools.bukkit.Plugin.PlInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
    private static String downloadLink = null;
    private static String latestVersion = null;

    public static boolean isUpToDate() throws IOException, ParseException {
        return !isOlderThan(PlInfo.VERSION, latestVersion);
    }

    public static boolean canCheckForUpdate() throws IOException, ParseException {
        String s = getLatestVersion();
        latestVersion = s;
        return s != null;
    }

    public static String getDownloadLink() {
        return downloadLink;
    }

    public static String getLatestVersion() throws IOException, ParseException {
        URL url = new URL("https://api.github.com/repos/ItzOverS/PowerToolS/releases");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("userAgent", "Mozilla/5.0");
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONParser json = new JSONParser();
            JSONArray js = (JSONArray) (json.parse(response.toString()));
            if (js.size() == 0) return null;
            int index = 0;
            while ((Boolean) ((JSONObject) js.get(index)).get("prerelease")) index++;
            JSONObject latestVersionJson = (JSONObject) js.get(index);
            downloadLink = (String) latestVersionJson.get("browser_download_url");
            return (String) latestVersionJson.get("tag_name");
        }
        return null;
    }

    private static boolean isOlderThan(String v1/*PowerToolS version*/, String v2) {
        if (Integer.parseInt(v1.split("\\.")[0]) > Integer.parseInt(v2.split("\\.")[0])) {
            return false;
        } else if (Integer.parseInt(v1.split("\\.")[0]) == Integer.parseInt(v2.split("\\.")[0])) {
            return isOlderThan(v1.substring(v1.split("\\.")[0].length() + 1), v2.substring(v2.split("\\.")[0].length() + 1));
        } else {
            return true;
        }
    }
}
