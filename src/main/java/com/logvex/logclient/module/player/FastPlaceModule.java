package com.logvex.logclient.module.player;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.IntSetting;
import com.logvex.logclient.setting.BooleanSetting;
import net.minecraft.client.MinecraftClient;

public class FastPlaceModule extends Module {
    private final IntSetting delay = integer("Delay", "Ticks between placements", 0, 0, 4);
    private final BooleanSetting onlyBlocks = bool("OnlyBlocks", "Only affect block placement", true);

    public FastPlaceModule() {
        super("FastPlace", "Removes the item use cooldown", Category.PLAYER, 0);
    }

    public boolean isOnlyBlocks() {
        return onlyBlocks.get();
    }

    public int getDelay() {
        return delay.get();
    }
}
