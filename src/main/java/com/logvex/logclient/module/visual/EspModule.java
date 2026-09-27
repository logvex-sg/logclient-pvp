package com.logvex.logclient.module.visual;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.EnumSetting;
import com.logvex.logclient.setting.IntSetting;
import com.logvex.logclient.util.ColorUtil;
import com.logvex.logclient.util.CombatTarget;
import com.logvex.logclient.util.PlayerUtil;
import com.logvex.logclient.util.RenderUtil;
import com.logvex.logclient.util.WorldRenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class EspModule extends Module {
    public enum Mode {
        BOX, OUTLINE, GLOW
    }

    public enum Targets {
        PLAYERS, MOBS, ALL
    }

    private final EnumSetting<Mode> mode = choice("Mode", "Rendering style", Mode.BOX);
    private final EnumSetting<Targets> targets = choice("Targets", "Which entities to highlight", Targets.PLAYERS);
    private final IntSetting color = integer("Color", "ARGB box color", 0xFFFF5555, Integer.MIN_VALUE, Integer.MAX_VALUE);
    private final IntSetting range = integer("Range", "Maximum distance in blocks", 96, 8, 256);
    private final IntSetting glowColor = integer("GlowColor", "ARGB glow color", 0xFFFF3B3B, Integer.MIN_VALUE, Integer.MAX_VALUE);
    private final IntSetting healthBarWidth = integer("HealthBar", "Health bar width in pixels, 0 disables", 2, 0, 6);

    public EspModule() {
        super("ESP", "Draws boxes around entities", Category.VISUAL, 0);
    }

    public Mode getMode() {
        return mode.getValue();
    }

    public Targets getTargets() {
        return targets.getValue();
    }

    public int getColor() {
        return color.get();
    }

    public int getRange() {
        return range.get();
    }

    public int getGlowColor() {
        return glowColor.get();
    }

    public boolean shouldGlow(Entity entity) {
        return isEnabled() && mode.getValue() == Mode.GLOW && isTarget(entity);
    }

    public boolean isTarget(Entity entity) {
        if (entity == MinecraftClient.getInstance().player || CombatTarget.isFriend(entity.getUuid())) {
            return false;
        }
        return switch (targets.getValue()) {
            case PLAYERS -> entity instanceof PlayerEntity;
            case MOBS -> entity instanceof HostileEntity;
            case ALL -> entity instanceof LivingEntity;
        };
    }

    private List<Entity> candidates(MinecraftClient mc) {
        List<Entity> list = new ArrayList<>();
        if (mc.world == null || mc.player == null) {
            return list;
        }
        double max = (double) range.get() * range.get();
        for (Entity entity : mc.world.getEntities()) {
            if (isTarget(entity) && mc.player.squaredDistanceTo(entity) <= max) {
                list.add(entity);
            }
        }
        return list;
    }

    public void renderWorld(DrawContext context, MinecraftClient mc, float tickDelta) {
        if (!isEnabled() || mc.world == null || mc.player == null || mode.getValue() == Mode.GLOW) {
            return;
        }
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();
        Matrix4f view = WorldRenderUtil.viewMatrix(mc.gameRenderer.getCamera());
        Matrix4f projection = WorldRenderUtil.projectionMatrix(mc.options.getFov().getValue(), width, height);

        for (Entity entity : candidates(mc)) {
            int boxColor = entity == CombatTarget.get() ? Theme.ACCENT : color.get();
            Vec3d lerped = entity.getLerpedPos(tickDelta);
            Box box = entity.getBoundingBox().offset(lerped.subtract(entity.getEntityPos()));
            if (mode.getValue() == Mode.BOX) {
                WorldRenderUtil.drawBox(context, view, projection, box, boxColor, tickDelta, mc);
            } else {
                drawOutline(context, mc, view, projection, box, boxColor, entity, tickDelta);
            }
        }
    }

    private void drawOutline(DrawContext context, MinecraftClient mc, Matrix4f view, Matrix4f projection,
                             Box box, int boxColor, Entity entity, float tickDelta) {
        Vector4f min = WorldRenderUtil.project(mc, view, projection, new Vec3d(box.minX, box.minY, box.minZ), tickDelta);
        Vector4f max = WorldRenderUtil.project(mc, view, projection, new Vec3d(box.maxX, box.maxY, box.maxZ), tickDelta);
        if (min == null || max == null) {
            return;
        }
        int screenWidth = mc.getWindow().getScaledWidth();
        int screenHeight = mc.getWindow().getScaledHeight();
        int x1 = WorldRenderUtil.screenX(min, screenWidth);
        int y1 = WorldRenderUtil.screenY(min, screenHeight);
        int x2 = WorldRenderUtil.screenX(max, screenWidth);
        int y2 = WorldRenderUtil.screenY(max, screenHeight);
        int left = Math.min(x1, x2);
        int top = Math.min(y1, y2);
        int boxWidth = Math.abs(x2 - x1);
        int boxHeight = Math.abs(y2 - y1);
        RenderUtil.outline(context, left, top, boxWidth, boxHeight, 1, boxColor);

        int barWidth = healthBarWidth.get();
        if (barWidth > 0 && entity instanceof LivingEntity living && living.getHealth() < living.getMaxHealth()) {
            float fraction = PlayerUtil.healthFraction(living);
            int filled = Math.round(boxHeight * fraction);
            RenderUtil.outline(context, left - barWidth - 2, top, barWidth, boxHeight, 1, 0xFF202020);
            context.fill(left - barWidth - 1, top + boxHeight - filled, left - 1, top + boxHeight,
                    ColorUtil.health(fraction));
        }
    }
}
