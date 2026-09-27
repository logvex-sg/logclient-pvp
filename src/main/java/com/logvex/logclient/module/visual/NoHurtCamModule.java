package com.logvex.logclient.module.visual;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;

public class NoHurtCamModule extends Module {
    private final BooleanSetting disableTilt = bool("DisableTilt", "Remove the hurt camera tilt", true);
    private final BooleanSetting disableBob = bool("DisableBob", "Remove view bobbing while hurt", false);

    public NoHurtCamModule() {
        super("NoHurtCam", "Removes the camera tilt when taking damage", Category.VISUAL, 0);
    }

    public boolean isDisableTilt() {
        return disableTilt.get();
    }

    public boolean isDisableBob() {
        return disableBob.get();
    }
}
