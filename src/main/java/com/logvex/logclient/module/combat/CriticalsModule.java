package com.logvex.logclient.module.combat;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;
import net.minecraft.client.MinecraftClient;

public class CriticalsModule extends Module {
    private final BooleanSetting onlyOnGround = bool("OnlyOnGround", "Only jump while grounded", true);
    private final BooleanSetting whileSprinting = bool("WhileSprinting", "Also jump while sprinting", true);

    public CriticalsModule() {
        super("Criticals", "Jumps before attacking to land critical hits", Category.COMBAT, 0);
    }

    public boolean shouldCrit() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!isEnabled() || mc.player == null) {
            return false;
        }
        if (onlyOnGround.get() && !mc.player.isOnGround()) {
            return false;
        }
        if (!whileSprinting.get() && mc.player.isSprinting()) {
            return false;
        }
        return true;
    }

    public void performCrit() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || !shouldCrit()) {
            return;
        }
        mc.player.jump();
    }
}
