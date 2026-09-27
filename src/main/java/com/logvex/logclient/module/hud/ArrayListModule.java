package com.logvex.logclient.module.hud;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.util.ColorUtil;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArrayListModule extends HudModule {
    public ArrayListModule() {
        super("ArrayList", "Lists every enabled module", 4, 68);
        bool("Rainbow", "Cycle through colors", false);
    }

    @Override
    public int getWidth(MinecraftClient mc) {
        return widest(mc) + 6;
    }

    @Override
    public int getHeight(MinecraftClient mc) {
        return entries().size() * (RenderUtil.fontHeight() + 2) + 4;
    }

    private List<Module> entries() {
        List<Module> modules = new ArrayList<>();
        for (Module module : LogClient.getInstance().getModuleManager().getModules()) {
            if (module.isEnabled() && !(module instanceof HudModule)) {
                modules.add(module);
            }
        }
        modules.sort(Comparator.comparingInt((Module m) -> -RenderUtil.textWidth(m.getName())));
        return modules;
    }

    private int widest(MinecraftClient mc) {
        int width = 0;
        for (Module module : entries()) {
            width = Math.max(width, RenderUtil.textWidth(module.getName()));
        }
        return width;
    }

    @Override
    public void render(DrawContext context, MinecraftClient mc, float tickDelta) {
        List<Module> modules = entries();
        if (modules.isEmpty()) {
            return;
        }
        int width = widest(mc) + 6;
        int lineHeight = RenderUtil.fontHeight() + 2;
        int screenWidth = mc.getWindow().getScaledWidth();
        int x = screenWidth - width - getX();
        int y = getY();
        boolean rainbow = getBoolean("Rainbow");
        int index = 0;
        for (Module module : modules) {
            int color = rainbow
                    ? ColorUtil.rainbow((System.currentTimeMillis() % 6000L) / 6000f - index * 0.05f, 0.6f, 0.95f)
                    : Theme.ACCENT;
            context.fill(x, y, screenWidth - getX(), y + lineHeight, Theme.HUD_BG);
            RenderUtil.text(context, module.getName(), x + 3, y + 1, color);
            y += lineHeight;
            index++;
        }
    }
}
