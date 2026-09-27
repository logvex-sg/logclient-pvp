package com.logvex.logclient.util;

public final class ColorUtil {
    private ColorUtil() {
    }

    public static int rgba(int r, int g, int b, int a) {
        return (clamp(a) << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);
    }

    public static int rgb(int r, int g, int b) {
        return rgba(r, g, b, 255);
    }

    private static int clamp(int value) {
        return value < 0 ? 0 : Math.min(value, 255);
    }

    public static int alpha(int color) {
        return (color >>> 24) & 0xFF;
    }

    public static int red(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int green(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int blue(int color) {
        return color & 0xFF;
    }

    public static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (clamp(alpha) << 24);
    }

    public static int scaleAlpha(int color, float factor) {
        return withAlpha(color, Math.round(alpha(color) * factor));
    }

    public static int lerp(int from, int to, float delta) {
        float t = Math.max(0f, Math.min(1f, delta));
        int a = Math.round(alpha(from) + (alpha(to) - alpha(from)) * t);
        int r = Math.round(red(from) + (red(to) - red(from)) * t);
        int g = Math.round(green(from) + (green(to) - green(from)) * t);
        int b = Math.round(blue(from) + (blue(to) - blue(from)) * t);
        return rgba(r, g, b, a);
    }

    public static int rainbow(float offset, float saturation, float lightness) {
        int rgb = java.awt.Color.HSBtoRGB((offset % 1f + 1f) % 1f, saturation, lightness);
        return withAlpha(rgb, 255);
    }

    public static int health(float fraction) {
        float t = Math.max(0f, Math.min(1f, fraction));
        return rgb(Math.round(255 * (1f - t)), Math.round(255 * t), 40);
    }

    public static int ping(int ms) {
        if (ms < 80) {
            return rgb(80, 220, 100);
        }
        if (ms < 160) {
            return rgb(230, 210, 60);
        }
        if (ms < 300) {
            return rgb(235, 150, 50);
        }
        return rgb(230, 70, 70);
    }
}
