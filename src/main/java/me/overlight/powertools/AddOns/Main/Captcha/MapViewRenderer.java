package me.overlight.powertools.AddOns.Main.Captcha;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class MapViewRenderer
        extends MapRenderer {
    boolean done = false;

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (done)
            return;
        Random random = new Random();
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        String num = "";
        for (int i = 0; i < 3; i++) {
            int rand = random.nextInt(10);
            num += rand;
            g.drawString(rand + "", (i + 1) * (random.nextInt(5) * 2), (i + 1) * random.nextInt(10));
        }
        g.dispose();
        img = MapPalette.resizeImage(img);
        mapCanvas.drawImage(0, 0, img);
        done = true;
    }
}
