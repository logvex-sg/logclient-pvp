package com.logvex.logclient.module.hud;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.util.MathUtil;
import com.logvex.logclient.util.PlayerUtil;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;

public class CoordinatesModule extends HudModule {
    public CoordinatesModule() {
        super("Coordinates", "Displays your position and facing direction", 4, 20);
    }

    private String value(MinecraftClient mc) {
        ClientPlayerEntity player = mc.player;
        if (player == null) {
            return "-";
        }
        return MathUtil.format(player.getX(), 1) + " " + MathUtil.format(player.getY(), 1) + " "
                + MathUtil.format(player.getZ(), 1) + " " + PlayerUtil.direction(player.getYaw());
    }

    @Override
    public int getWidth(MinecraftClient mc) {
        return labelValueWidth("XYZ", value(mc));
    }

    @Override
    public int getHeight(MinecraftClient mc) {
        return RenderUtil.fontHeight() + 4;
    }

    @Override
    public void render(DrawContext context, MinecraftClient mc, float tickDelta) {
        renderLabelValue(context, mc, "XYZ", value(mc));
    }
}
