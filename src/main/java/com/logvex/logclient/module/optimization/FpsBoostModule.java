package com.logvex.logclient.module.optimization;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;

public class FpsBoostModule extends Module {
    private final BooleanSetting noParticles = bool("NoParticles", "Stop rendering particles", false);
    private final BooleanSetting noClouds = bool("NoClouds", "Hide clouds", true);
    private final BooleanSetting noBlockBreak = bool("NoBlockBreak", "Hide block break particles", true);
    private final BooleanSetting noNametags = bool("NoNametags", "Hide vanilla name tags", false);

    public FpsBoostModule() {
        super("FpsBoost", "Aggressive visual reductions for maximum framerate", Category.OPTIMIZATION, 0);
    }

    public boolean isNoParticles() {
        return noParticles.get();
    }

    public boolean isNoClouds() {
        return noClouds.get();
    }

    public boolean isNoBlockBreak() {
        return noBlockBreak.get();
    }

    public boolean isNoNametags() {
        return noNametags.get();
    }
}
