package com.logvex.logclient.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;

public class GuiManager {
    private ClickGuiScreen screen;

    public void toggle() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen instanceof ClickGuiScreen) {
            mc.setScreen(null);
        } else if (mc.currentScreen == null) {
            if (screen == null) {
                screen = new ClickGuiScreen();
            }
            mc.setScreen(screen);
        }
    }

    public boolean isOpen() {
        return MinecraftClient.getInstance().currentScreen instanceof ClickGuiScreen;
    }

    public boolean isClientScreen(Screen screen) {
        return screen instanceof ClickGuiScreen;
    }

    /** Called right after the screen switch, which cleared every pressed key state. */
    public void restoreMovementKeys() {
        KeyBinding.updatePressedStates();
    }

    public void unpressMovementKeys() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options == null) {
            return;
        }
        for (KeyBinding binding : new KeyBinding[]{
                mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey,
                mc.options.jumpKey, mc.options.sprintKey}) {
            binding.setPressed(false);
        }
    }
}
