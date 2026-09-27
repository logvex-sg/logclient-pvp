package com.logvex.logclient.module.misc;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.EnumSetting;
import net.minecraft.client.MinecraftClient;

public class CopyCoordsModule extends Module {
    public enum Format {
        PLAIN, COMMAND, LABELLED
    }

    private final EnumSetting<Format> format = choice("Format", "Clipboard layout", Format.COMMAND);

    public CopyCoordsModule() {
        super("CopyCoords", "Copies your coordinates to the clipboard", Category.MISC, 0);
    }

    @Override
    public boolean isToggleable() {
        return false;
    }

    @Override
    public void onKeybind() {
        copy();
    }

    public void copy() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.keyboard == null) {
            return;
        }
        String text = switch (format.getValue()) {
            case PLAIN -> String.format("%.1f %.1f %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ());
            case COMMAND -> String.format("/tp %.1f %.1f %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ());
            case LABELLED -> String.format("X: %.1f Y: %.1f Z: %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ());
        };
        mc.keyboard.setClipboard(text);
    }
}
