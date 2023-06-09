package me.overlight.powertools.bukkit.Libraries;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageMapLoader
        extends MapRenderer {
    private boolean isDone = false;
    private BufferedImage image = null;

    public boolean loadImage(String url) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new URL(url));
            img = MapPalette.resizeImage(img);
        } catch (IOException e) {
            return false;
        }
        this.image = img;
        return true;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (isDone) return;
        mapCanvas.drawImage(0, 0, image);
        isDone = true;
    }
}
