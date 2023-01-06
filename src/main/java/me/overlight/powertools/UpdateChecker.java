package me.overlight.powertools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.overlight.powertools.Plugin.PlInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class UpdateChecker {
    public static String downloadLink = null;
    public static boolean isUpToDate() throws IOException, ParseException {
        return Objects.equals(getLatestVersion(), PlInfo.VERSION);
    }

    public static String getDownloadLink(){
        return downloadLink;
    }

    public static String getLatestVersion() throws IOException, ParseException {
        URL url = new URL("https://api.github.com/repos/ItzOverS/PowerToolS/releases");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("userAgent", "Mozilla/5.0");
        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            } in.close();
            JSONParser json = new JSONParser();
            JSONArray js = (JSONArray)(json.parse(response.toString()));
            if(js.size() == 0) return null;
            JSONObject latestVersionJson = (JSONObject) js.get(js.size() - 1);
            downloadLink = (String) latestVersionJson.get("browser_download_url");
            return (String) latestVersionJson.get("tag_name");
        }
        return null;
    }
}
