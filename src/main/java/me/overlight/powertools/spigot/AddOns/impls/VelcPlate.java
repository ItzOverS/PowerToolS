package me.overlight.powertools.spigot.AddOns.impls;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class VelcPlate {
    private final Location loc;

    public enum Mode {
        Force,
        Head
    }

    private final Mode mode;
    private final double multiply;
    private final double verticalMultiply;
    private final Vector knockback;

    public VelcPlate(Location loc, Mode mode, double multiply, double verticalMultiply, Vector knockback) {
        this.loc = loc;
        this.mode = mode;
        this.multiply = multiply;
        this.knockback = knockback;
        this.verticalMultiply = verticalMultiply;
    }

    public Location getLoc() {
        return loc;
    }

    public Mode getMode() {
        return mode;
    }

    public double getMultiply() {
        return multiply;
    }

    public Vector getKnockback() {
        return knockback;
    }

    public double getVerticalMultiply() {
        return verticalMultiply;
    }
}
