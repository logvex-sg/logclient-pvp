package com.logvex.logclient.module.hud;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.util.ColorUtil;
import com.logvex.logclient.util.CombatTarget;
import com.logvex.logclient.util.PlayerUtil;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class TargetHudModule extends HudModule {
    public TargetHudModule() {
        super("TargetHUD", "Shows information about your current target", 4, 132);
        bool("ShowHealth", "Display numeric health", true);
    }

    @Override
    public int getWidth(MinecraftClient mc) {
        return 150;
    }

    @Override
    public int getHeight(MinecraftClient mc) {
        return 42;
    }

    @Override
    public void render(DrawContext context, MinecraftClient mc, float tickDelta) {
        LivingEntity target = CombatTarget.get();
        if (target == null || target == mc.player) {
            return;
        }
        int x = getX();
        int y = getY();
        int width = getWidth(mc);
        int height = getHeight(mc);

        context.fill(x, y, x + width, y + height, Theme.PANEL);
        RenderUtil.outline(context, x, y, width, height, 1, Theme.PANEL_BORDER);

        String name = target.getName().getString();
        RenderUtil.text(context, name, x + 6, y + 5, Theme.TEXT);

        String ping = "";
        if (target instanceof AbstractClientPlayerEntity player) {
            ping = PlayerUtil.ping(player) + "ms";
        }
        RenderUtil.text(context, ping, x + width - 6 - RenderUtil.textWidth(ping), y + 5, Theme.TEXT_DIM);

        float health = PlayerUtil.health(target);
        float max = target.getMaxHealth();
        float fraction = max <= 0 ? 0f : Math.min(1f, health / max);
        int barY = y + 18;
        context.fill(x + 6, barY, x + width - 6, barY + 8, Theme.HUD_BG);
        context.fill(x + 6, barY, x + 6 + Math.round((width - 12) * fraction), barY + 8, ColorUtil.health(fraction));

        if (getBoolean("ShowHealth")) {
            String hp = String.format("%.1f", health);
            RenderUtil.centeredText(context, hp, x + width / 2, barY + 1, Theme.TEXT);
        }

        int itemX = x + 6;
        int itemY = y + height - 18;
        ItemStack main = target.getEquippedStack(net.minecraft.entity.EquipmentSlot.MAINHAND);
        if (!main.isEmpty()) {
            context.drawItem(main, itemX, itemY);
            itemX += 18;
        }
        ItemStack off = target.getEquippedStack(net.minecraft.entity.EquipmentSlot.OFFHAND);
        if (!off.isEmpty()) {
            context.drawItem(off, itemX, itemY);
        }

        String armor = "Armor " + target.getArmor();
        RenderUtil.text(context, armor, x + width - 6 - RenderUtil.textWidth(armor), y + height - 14, Theme.TEXT_DIM);
    }
}
