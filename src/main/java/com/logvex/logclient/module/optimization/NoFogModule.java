package com.logvex.logclient.module.optimization;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;

public class NoFogModule extends Module {
    private final BooleanSetting waterFog = bool("Water", "Remove water fog", true);
    private final BooleanSetting lavaFog = bool("Lava", "Remove lava fog", true);
    private final BooleanSetting powderFog = bool("PowderSnow", "Remove powder snow fog", true);
    private final BooleanSetting blindness = bool("Blindness", "Remove blindness and darkness fog", true);

    public NoFogModule() {
        super("NoFog", "Removes fog in water, lava and powder snow", Category.OPTIMIZATION, 0);
    }

    public boolean isWaterFog() {
        return waterFog.get();
    }

    public boolean isLavaFog() {
        return lavaFog.get();
    }

    public boolean isPowderFog() {
        return powderFog.get();
    }

    public boolean isBlindness() {
        return blindness.get();
    }
}
