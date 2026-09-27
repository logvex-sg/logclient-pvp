package com.logvex.logclient.module.optimization;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.IntSetting;

public class ParticleLimitModule extends Module {
    private final IntSetting limit = integer("Limit", "Maximum particles spawned per tick", 500, 0, 20000);

    public ParticleLimitModule() {
        super("ParticleLimit", "Caps how many particles can spawn", Category.OPTIMIZATION, 0);
    }

    public int getLimit() {
        return limit.get();
    }
}
