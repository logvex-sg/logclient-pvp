package com.logvex.logclient.module.visual;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.DoubleSetting;
import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {
    private final DoubleSetting gamma = decimal("Gamma", "Brightness level", 10.0, 1.0, 16.0);

    private Double previousGamma;

    public FullbrightModule() {
        super("Fullbright", "Raises the brightness to see in the dark", Category.VISUAL, 0);
    }

    @Override
    public void onEnable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            previousGamma = mc.options.getGamma().getValue();
            mc.options.getGamma().setValue(gamma.get());
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            mc.options.getGamma().setValue(previousGamma == null ? 1.0 : previousGamma);
        }
        previousGamma = null;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null && mc.options.getGamma().getValue() != gamma.get()) {
            mc.options.getGamma().setValue(gamma.get());
        }
    }
}
