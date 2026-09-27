package com.logvex.logclient.module.hud;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.util.ColorUtil;
import com.logvex.logclient.util.PlayerUtil;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;

public class PingModule extends HudModule {
    public PingModule() {
        super("Ping", "Displays your current latency", 4, 36);
    }

    private int latency(MinecraftClient mc) {
        ClientPlayerEntity player = mc.player;
        return player == null ? 0 : PlayerUtil.ping(player);
    }

    @Override
    public int getWidth(MinecraftClient mc) {
        return labelValueWidth("Ping", latency(mc) + "ms");
    }

    @Override
    public int getHeight(MinecraftClient mc) {
        return RenderUtil.fontHeight() + 4;
    }

    @Override
    public void render(DrawContext context, MinecraftClient mc, float tickDelta) {
        int latency = latency(mc);
        drawBackground(context, getX(), getY(), getWidth(mc), getHeight(mc));
        RenderUtil.text(context, "Ping", getX() + 2, getY() + 2, getColor());
        RenderUtil.text(context, latency + "ms", getX() + 2 + RenderUtil.textWidth("Ping "), getY() + 2, ColorUtil.ping(latency));
    }
}
