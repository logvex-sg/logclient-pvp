package com.logvex.logclient.module.hud;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FpsModule extends HudModule {
    public FpsModule() {
        super("FPS", "Displays the current framerate", 4, 4);
    }

    @Override
    public int getWidth(MinecraftClient mc) {
        return labelValueWidth("FPS", String.valueOf(mc.getCurrentFps()));
    }

    @Override
    public int getHeight(MinecraftClient mc) {
        return RenderUtil.fontHeight() + 4;
    }

    @Override
    public void render(DrawContext context, MinecraftClient mc, float tickDelta) {
        renderLabelValue(context, mc, "FPS", String.valueOf(mc.getCurrentFps()));
    }
}
