package com.logvex.logclient.module.hud;

import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class SpeedModule extends HudModule {
    public SpeedModule() {
        super("Speed", "Displays horizontal movement speed in blocks per tick", 4, 52);
    }

    private String value(MinecraftClient mc) {
        if (mc.player == null) {
            return "0.00";
        }
        double dx = mc.player.getX() - mc.player.lastX;
        double dz = mc.player.getZ() - mc.player.lastZ;
        return String.format("%.2f", Math.sqrt(dx * dx + dz * dz));
    }

    @Override
    public int getWidth(MinecraftClient mc) {
        return labelValueWidth("Speed", value(mc));
    }

    @Override
    public int getHeight(MinecraftClient mc) {
        return RenderUtil.fontHeight() + 4;
    }

    @Override
    public void render(DrawContext context, MinecraftClient mc, float tickDelta) {
        renderLabelValue(context, mc, "Speed", value(mc));
    }
}
