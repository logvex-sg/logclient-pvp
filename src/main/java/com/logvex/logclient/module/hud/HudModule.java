package com.logvex.logclient.module.hud;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.DoubleSetting;
import com.logvex.logclient.setting.IntSetting;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.joml.Matrix3x2fStack;

public abstract class HudModule extends Module {
    private final IntSetting x = integer("X", "Horizontal screen position", 4, 0, 4000);
    private final IntSetting y = integer("Y", "Vertical screen position", 4, 0, 4000);
    private final DoubleSetting scale = decimal("Scale", "HUD element scale", 1.0, 0.5, 2.0);
    private final IntSetting color = integer("Color", "ARGB accent color", 0xFF4FC3F7, Integer.MIN_VALUE, Integer.MAX_VALUE);

    protected HudModule(String name, String description, int defaultX, int defaultY) {
        super(name, description, Category.HUD, 0);
        x.set(defaultX);
        y.set(defaultY);
    }

    public int getX() {
        return x.get();
    }

    public void setX(int value) {
        x.set(value);
    }

    public int getY() {
        return y.get();
    }

    public void setY(int value) {
        y.set(value);
    }

    public float getScale() {
        return scale.getFloat();
    }

    public int getColor() {
        return color.get();
    }

    public IntSetting getColorSetting() {
        return color;
    }

    public abstract int getWidth(MinecraftClient mc);

    public abstract int getHeight(MinecraftClient mc);

    public abstract void render(DrawContext context, MinecraftClient mc, float tickDelta);

    public void renderScaled(DrawContext context, MinecraftClient mc, float tickDelta) {
        float s = getScale();
        if (Math.abs(s - 1.0f) < 0.001f) {
            render(context, mc, tickDelta);
            return;
        }
        Matrix3x2fStack matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.translate(getX(), getY());
        matrices.scale(s, s);
        matrices.translate(-getX(), -getY());
        render(context, mc, tickDelta);
        matrices.popMatrix();
    }

    protected void drawBackground(DrawContext context, int x, int y, int width, int height) {
        context.fill(x - 1, y - 1, x + width + 1, y + height + 1, Theme.HUD_BG);
    }

    protected int labelValueWidth(String label, String value) {
        return RenderUtil.textWidth(label + " " + value) + 4;
    }

    protected void renderLabelValue(DrawContext context, MinecraftClient mc, String label, String value) {
        drawBackground(context, getX(), getY(), getWidth(mc), getHeight(mc));
        RenderUtil.text(context, label, getX() + 2, getY() + 2, getColor());
        RenderUtil.text(context, value, getX() + 2 + RenderUtil.textWidth(label + " "), getY() + 2, Theme.TEXT);
    }
}
