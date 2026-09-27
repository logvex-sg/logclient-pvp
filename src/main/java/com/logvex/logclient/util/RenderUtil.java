package com.logvex.logclient.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class RenderUtil {
    private RenderUtil() {
    }

    public static void text(DrawContext context, String value, int x, int y, int color) {
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, value, x, y, color);
    }

    public static void textNoShadow(DrawContext context, String value, int x, int y, int color) {
        context.drawText(MinecraftClient.getInstance().textRenderer, value, x, y, color, false);
    }

    public static void centeredText(DrawContext context, String value, int centerX, int y, int color) {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, value, centerX, y, color);
    }

    public static int textWidth(String value) {
        return MinecraftClient.getInstance().textRenderer.getWidth(value);
    }

    public static int fontHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    public static void rect(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public static void outline(DrawContext context, int x, int y, int width, int height, int thickness, int color) {
        context.fill(x, y, x + width, y + thickness, color);
        context.fill(x, y + height - thickness, x + width, y + height, color);
        context.fill(x, y + thickness, x + thickness, y + height - thickness, color);
        context.fill(x + width - thickness, y + thickness, x + width, y + height - thickness, color);
    }

    /** Bresenham line so overlays can be drawn with the GUI renderer. */
    public static void line(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int error = dx - dy;
        int x = x1;
        int y = y1;
        while (true) {
            context.fill(x, y, x + 1, y + 1, color);
            if (x == x2 && y == y2) {
                return;
            }
            int doubled = error * 2;
            if (doubled > -dy) {
                error -= dy;
                x += sx;
            }
            if (doubled < dx) {
                error += dx;
                y += sy;
            }
        }
    }

    public static void gradient(DrawContext context, int x, int y, int width, int height, int top, int bottom) {
        context.fillGradient(x, y, x + width, y + height, top, bottom);
    }

    public static void push(DrawContext context) {
        context.getMatrices().pushMatrix();
    }

    public static void pop(DrawContext context) {
        context.getMatrices().popMatrix();
    }

    public static void translate(DrawContext context, float x, float y) {
        context.getMatrices().translate(x, y);
    }

    public static void scale(DrawContext context, float x, float y) {
        context.getMatrices().scale(x, y);
    }

    public static void scissor(DrawContext context, int x, int y, int width, int height) {
        context.enableScissor(x, y, x + width, y + height);
    }

    public static void resetScissor(DrawContext context) {
        context.disableScissor();
    }
}
