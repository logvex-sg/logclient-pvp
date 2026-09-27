package com.logvex.logclient.gui;

import com.logvex.logclient.util.ColorUtil;

public final class Theme {
    private Theme() {
    }

    public static final int BACKGROUND = ColorUtil.rgba(14, 15, 20, 235);
    public static final int PANEL = ColorUtil.rgba(22, 24, 32, 240);
    public static final int PANEL_HEADER = ColorUtil.rgba(30, 33, 44, 255);
    public static final int PANEL_BORDER = ColorUtil.rgba(60, 66, 84, 255);
    public static final int ACCENT = ColorUtil.rgba(79, 195, 247, 255);
    public static final int ACCENT_DARK = ColorUtil.rgba(41, 128, 185, 255);
    public static final int TEXT = ColorUtil.rgba(235, 238, 245, 255);
    public static final int TEXT_DIM = ColorUtil.rgba(150, 156, 172, 255);
    public static final int ENABLED = ColorUtil.rgba(90, 220, 140, 255);
    public static final int DISABLED = ColorUtil.rgba(110, 114, 128, 255);
    public static final int HUD_BG = ColorUtil.rgba(0, 0, 0, 120);
    public static final int WIDGET_HOVER = ColorUtil.rgba(255, 255, 255, 28);
    public static final int WIDGET_ACTIVE = ColorUtil.rgba(79, 195, 247, 45);
}
