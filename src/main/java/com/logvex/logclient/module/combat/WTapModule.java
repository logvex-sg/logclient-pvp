package com.logvex.logclient.module.combat;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;
import com.logvex.logclient.setting.IntSetting;
import net.minecraft.client.MinecraftClient;

public class WTapModule extends Module {
    private final IntSetting releaseTicks = integer("ReleaseTicks", "Ticks to release sprint for", 1, 1, 5);
    private final BooleanSetting onlyOnGround = bool("OnlyOnGround", "Only tap while grounded", false);

    private int releaseTimer;

    public WTapModule() {
        super("WTap", "Releases sprint on hit to reset knockback", Category.COMBAT, 0);
    }

    @Override
    public void onDisable() {
        releaseTimer = 0;
    }

    public void onAttack() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!isEnabled() || mc.player == null) {
            return;
        }
        if (onlyOnGround.get() && !mc.player.isOnGround()) {
            return;
        }
        if (mc.player.isSprinting()) {
            mc.player.setSprinting(false);
            releaseTimer = releaseTicks.get();
        }
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || releaseTimer <= 0) {
            return;
        }
        releaseTimer--;
        if (releaseTimer == 0 && mc.options.sprintKey.isPressed()) {
            mc.player.setSprinting(true);
        }
    }
}
