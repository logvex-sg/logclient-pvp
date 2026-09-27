package com.logvex.logclient.module.player;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class SafeWalkModule extends Module {
    private final BooleanSetting onlyWhileSneaking = bool("OnlySneak", "Only act while the sneak key is held", true);

    public SafeWalkModule() {
        super("SafeWalk", "Stops you walking off ledges", Category.PLAYER, 0);
    }

    public boolean shouldHold(MinecraftClient mc) {
        if (!isEnabled() || mc.player == null || mc.world == null || !mc.player.isOnGround()) {
            return false;
        }
        if (onlyWhileSneaking.get() && !mc.options.sneakKey.isPressed()) {
            return false;
        }
        if (!mc.options.forwardKey.isPressed() || mc.player.isSneaking()) {
            return false;
        }
        BlockPos ahead = BlockPos.ofFloored(
                mc.player.getX() + mc.player.getVelocity().x * 2.5,
                mc.player.getY() - 1.0,
                mc.player.getZ() + mc.player.getVelocity().z * 2.5);
        return mc.world.getBlockState(ahead).isAir();
    }
}
