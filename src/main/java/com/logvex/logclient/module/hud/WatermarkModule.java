package com.logvex.logclient.module.hud;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class WatermarkModule extends HudModule {
    public WatermarkModule() {
        super("Watermark", "Displays the client name and version", 4, 84);
    }

    private String label() {
        return "LogClient " + LogClient.VERSION;
    }

    @Override
    public int getWidth(MinecraftClient mc) {
        return RenderUtil.textWidth(label()) + 6;
    }

    @Override
    public int getHeight(MinecraftClient mc) {
        return RenderUtil.fontHeight() + 4;
    }

    @Override
    public void render(DrawContext context, MinecraftClient mc, float tickDelta) {
        drawBackground(context, getX(), getY(), getWidth(mc), getHeight(mc));
        RenderUtil.text(context, label(), getX() + 3, getY() + 2, Theme.ACCENT);
    }
}
