package com.logvex.logclient.util;

import com.logvex.logclient.LogClient;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class KeybindManager {
    public static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of("logclient", "main"));

    private static KeyBinding openGui;
    private static boolean initialized;

    private KeybindManager() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.logclient.open_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, CATEGORY));
    }

    public static void handleInput() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getWindow() == null) {
            return;
        }
        while (openGui != null && openGui.wasPressed()) {
            LogClient.getInstance().getGuiManager().toggle();
        }
    }
}
