package com.logvex.logclient.module.optimization;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityCullingModule extends Module {
    private final IntSetting distance = integer("Distance", "Hide entities beyond this many blocks", 64, 8, 256);

    public EntityCullingModule() {
        super("EntityCulling", "Skips rendering distant entities", Category.OPTIMIZATION, 0);
    }

    public boolean shouldCull(Entity entity) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!isEnabled() || mc.player == null || entity == mc.player || entity instanceof PlayerEntity) {
            return false;
        }
        double max = (double) distance.get() * distance.get();
        return mc.player.squaredDistanceTo(entity) > max;
    }
}
