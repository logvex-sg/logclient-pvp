package com.logvex.logclient.module.hud;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PotionModule extends HudModule {
    public PotionModule() {
        super("PotionStatus", "Lists active status effects", 4, 116);
    }

    private List<StatusEffectInstance> effects(MinecraftClient mc) {
        List<StatusEffectInstance> list = new ArrayList<>();
        if (mc.player != null) {
            list.addAll(mc.player.getStatusEffects());
            list.sort(Comparator.comparingInt(StatusEffectInstance::getDuration).reversed());
        }
        return list;
    }

    private String line(StatusEffectInstance effect) {
        String name = effect.getEffectType().value().getName().getString();
        String duration = effect.isInfinite() ? "**" : format(effect.getDuration());
        String amplifier = effect.getAmplifier() > 0 ? " " + roman(effect.getAmplifier() + 1) : "";
        return name + amplifier + " " + duration;
    }

    private String format(int ticks) {
        int seconds = ticks / 20;
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    private String roman(int value) {
        return switch (value) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> String.valueOf(value);
        };
    }

    @Override
    public int getWidth(MinecraftClient mc) {
        int width = 0;
        for (StatusEffectInstance effect : effects(mc)) {
            width = Math.max(width, RenderUtil.textWidth(line(effect)));
        }
        return width + 6;
    }

    @Override
    public int getHeight(MinecraftClient mc) {
        return effects(mc).size() * (RenderUtil.fontHeight() + 2) + 4;
    }

    @Override
    public void render(DrawContext context, MinecraftClient mc, float tickDelta) {
        List<StatusEffectInstance> effects = effects(mc);
        if (effects.isEmpty()) {
            return;
        }
        drawBackground(context, getX(), getY(), getWidth(mc), getHeight(mc));
        int y = getY() + 2;
        for (StatusEffectInstance effect : effects) {
            RenderUtil.text(context, line(effect), getX() + 3, y, Theme.TEXT);
            y += RenderUtil.fontHeight() + 2;
        }
    }
}
