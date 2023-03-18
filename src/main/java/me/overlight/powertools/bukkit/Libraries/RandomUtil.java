package me.overlight.powertools.bukkit.Libraries;

import java.util.Arrays;
import java.util.Random;

public class RandomUtil {
    public static int randomInteger(int from, int to) {
        Random r = new Random();
        return r.nextInt(from) + (to - from);
    }

    public static int randomInteger() {
        Random r = new Random();
        return r.nextInt();
    }

    public static Object selectObject(Object... objs) {
        Random r = new Random();
        if (Arrays.asList(objs).isEmpty()) return null;
        return Arrays.asList(objs).get(r.nextInt(objs.length));
    }
}
