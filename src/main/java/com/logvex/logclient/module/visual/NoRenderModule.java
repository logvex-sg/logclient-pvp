package com.logvex.logclient.module.visual;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;

public class NoRenderModule extends Module {
    public static final String FIRE = "Fire";
    public static final String WATER = "Water";
    public static final String PUMPKIN = "Pumpkin";
    public static final String VIGNETTE = "Vignette";
    public static final String PORTAL = "Portal";
    public static final String NAUSEA = "Nausea";
    public static final String POWDER_SNOW = "PowderSnow";
    public static final String TOTEM = "Totem";

    private final BooleanSetting fire = bool("Fire", "Hide the fire overlay", false);
    private final BooleanSetting water = bool("Water", "Hide the underwater overlay", false);
    private final BooleanSetting pumpkin = bool("Pumpkin", "Hide the pumpkin blur", false);
    private final BooleanSetting vignette = bool("Vignette", "Hide the vignette", false);
    private final BooleanSetting portal = bool("Portal", "Hide the portal overlay", false);
    private final BooleanSetting nausea = bool("Nausea", "Hide the nausea overlay", false);
    private final BooleanSetting powderSnow = bool("PowderSnow", "Hide the powder snow overlay", false);
    private final BooleanSetting totem = bool("Totem", "Hide the totem animation", false);

    public NoRenderModule() {
        super("NoRender", "Hides selected screen overlays", Category.VISUAL, 0);
    }

    public boolean isNoRenderEnabled(String option) {
        return switch (option) {
            case FIRE -> fire.get();
            case WATER -> water.get();
            case PUMPKIN -> pumpkin.get();
            case VIGNETTE -> vignette.get();
            case PORTAL -> portal.get();
            case NAUSEA -> nausea.get();
            case POWDER_SNOW -> powderSnow.get();
            case TOTEM -> totem.get();
            default -> false;
        };
    }
}
