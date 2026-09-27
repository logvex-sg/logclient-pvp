package com.logvex.logclient.module.optimization;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;

public class NoWeatherModule extends Module {
    public NoWeatherModule() {
        super("NoWeather", "Removes rain and snow rendering", Category.OPTIMIZATION, 0);
    }
}
