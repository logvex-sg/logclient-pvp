package com.logvex.logclient.util;

public final class MathUtil {
    private MathUtil() {
    }

    public static float lerp(float from, float to, float delta) {
        return from + (to - from) * delta;
    }

    public static double lerp(double from, double to, double delta) {
        return from + (to - from) * delta;
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : Math.min(value, max);
    }

    public static double clamp(double value, double min, double max) {
        return value < min ? min : Math.min(value, max);
    }

    public static int clamp(int value, int min, int max) {
        return value < min ? min : Math.min(value, max);
    }

    public static String format(double value, int decimals) {
        return String.format("%." + decimals + "f", value);
    }

    public static double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }
}
