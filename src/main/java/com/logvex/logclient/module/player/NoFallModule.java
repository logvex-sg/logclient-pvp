package com.logvex.logclient.module.player;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;
import net.minecraft.client.MinecraftClient;

public class NoFallModule extends Module {
    private final BooleanSetting onlyWhenFalling = bool("OnlyFalling", "Only send packets while falling", true);

    public NoFallModule() {
        super("NoFall", "Prevents fall damage by reporting you as grounded", Category.PLAYER, 0);
    }

    public boolean shouldSendPacket(MinecraftClient mc) {
        if (!isEnabled() || mc.player == null) {
            return false;
        }
        if (mc.player.isOnGround() || mc.player.getAbilities().flying || mc.player.isGliding()) {
            return false;
        }
        return !onlyWhenFalling.get() || mc.player.fallDistance > 2.5;
    }
}
