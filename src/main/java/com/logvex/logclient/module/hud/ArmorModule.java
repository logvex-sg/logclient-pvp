package com.logvex.logclient.module.hud;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.util.ColorUtil;
import com.logvex.logclient.util.PlayerUtil;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ArmorModule extends HudModule {
    private static final List<EquipmentSlot> SLOTS = List.of(
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

    public ArmorModule() {
        super("Armor", "Displays armor durability", 4, 100);
    }

    @Override
    public int getWidth(MinecraftClient mc) {
        return SLOTS.size() * 18 + 4;
    }

    @Override
    public int getHeight(MinecraftClient mc) {
        return 24;
    }

    @Override
    public void render(DrawContext context, MinecraftClient mc, float tickDelta) {
        if (mc.player == null) {
            return;
        }
        drawBackground(context, getX(), getY(), getWidth(mc), getHeight(mc));
        int x = getX() + 2;
        for (EquipmentSlot slot : SLOTS) {
            ItemStack stack = mc.player.getEquippedStack(slot);
            if (!stack.isEmpty()) {
                context.drawItem(stack, x, getY() + 2);
                float fraction = PlayerUtil.durabilityFraction(stack);
                if (fraction < 1f) {
                    context.fill(x + 1, getY() + 19, x + 15, getY() + 21, Theme.HUD_BG);
                    context.fill(x + 1, getY() + 19, x + 1 + Math.round(14 * fraction), getY() + 21,
                            ColorUtil.health(fraction));
                }
            }
            x += 18;
        }
    }
}
