package com.logvex.logclient.module.visual;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.IntSetting;
import com.logvex.logclient.util.ColorUtil;
import com.logvex.logclient.util.PlayerUtil;
import com.logvex.logclient.util.WorldRenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.joml.Matrix4f;

public class NameTagsModule extends Module {
    private final IntSetting range = integer("Range", "Maximum distance in blocks", 64, 8, 256);
    private final IntSetting background = integer("Background", "ARGB label background", 0x80000000, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public NameTagsModule() {
        super("NameTags", "Improved player name tags", Category.VISUAL, 0);
    }

    public void renderWorld(DrawContext context, MinecraftClient mc, float tickDelta) {
        if (!isEnabled() || mc.world == null || mc.player == null) {
            return;
        }
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();
        Matrix4f view = WorldRenderUtil.viewMatrix(mc.gameRenderer.getCamera());
        Matrix4f projection = WorldRenderUtil.projectionMatrix(mc.options.getFov().getValue(), width, height);
        double max = (double) range.get() * range.get();

        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || player.isInvisible() || mc.player.squaredDistanceTo(player) > max) {
                continue;
            }
            String name = player.getName().getString();
            String detail = String.format("%.1f HP  %.0fm", PlayerUtil.health(player), PlayerUtil.distance(mc.player, player));
            WorldRenderUtil.drawEntityLabel(context, view, projection, player, name, Theme.TEXT, background.get(), tickDelta, mc);
            WorldRenderUtil.drawEntityLabel(context, view, projection, player, detail,
                    ColorUtil.ping(PlayerUtil.ping(player)), background.get(), tickDelta, mc);
        }
    }
}
