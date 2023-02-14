package me.overlight.powertools.spigot.AddOns.Main.AntiBot;

import me.overlight.powertools.spigot.PowerTools;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MapGen
        extends MapRenderer {
    boolean done = false;

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (done)
            return;
        Random random = new Random();
        int num = random.nextInt(PowerTools.config.getConfigurationSection("Captcha.MapsLink").getKeys(false).size());
        AntiBot.playerCodes.put(player.getName(), new ArrayList<>(PowerTools.config.getConfigurationSection("Captcha.MapsLink").getKeys(false)).get(num));
        BufferedImage img = null;
        try {
            img = ImageIO.read(new URL(new ArrayList<>(PowerTools.config.getConfigurationSection("Captcha.MapsLink").getKeys(false)).get(num)));
        } catch (IOException ignored) {
        }
        img = MapPalette.resizeImage(img);
        mapCanvas.drawImage(0, 0, img);
        done = true;
    }
}
