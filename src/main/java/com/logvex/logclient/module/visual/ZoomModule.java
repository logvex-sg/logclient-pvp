package com.logvex.logclient.module.visual;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;
import com.logvex.logclient.setting.DoubleSetting;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class ZoomModule extends Module {
    private final DoubleSetting divisor = decimal("Divisor", "Zoom factor", 4.0, 1.5, 10.0);
    private final BooleanSetting scrollZoom = bool("ScrollZoom", "Adjust zoom with the mouse wheel", true);

    private boolean zooming;

    public ZoomModule() {
        super("Zoom", "Hold a key to zoom in", Category.VISUAL, 0);
    }

    public float getDivisor() {
        return divisor.getFloat();
    }

    public boolean isScrollZoom() {
        return scrollZoom.get();
    }

    public boolean isZooming() {
        return zooming;
    }

    public void onScroll(double amount) {
        if (!isScrollZoom() || !zooming) {
            return;
        }
        divisor.setValue(Math.max(1.5, Math.min(10.0, divisor.get() - amount)));
    }

    @Override
    public void onDisable() {
        zooming = false;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        zooming = mc.getWindow() != null && mc.currentScreen == null
                && GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS;
    }
}
