package com.logvex.logclient.module.combat;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class VelocityModule extends Module {
    private final IntSetting horizontalPercent = integer("Horizontal", "Horizontal knockback percent", 0, 0, 100);
    private final IntSetting verticalPercent = integer("Vertical", "Vertical knockback percent", 0, 0, 100);

    public VelocityModule() {
        super("Velocity", "Reduces knockback taken", Category.COMBAT, 0);
    }

    public Vec3d modify(MinecraftClient mc, Vec3d velocity) {
        if (!isEnabled() || mc.player == null) {
            return velocity;
        }
        double horizontal = horizontalPercent.get() / 100.0;
        double vertical = verticalPercent.get() / 100.0;
        return new Vec3d(velocity.x * horizontal, velocity.y * vertical, velocity.z * horizontal);
    }
}
