package com.logvex.logclient.gui.widget;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.setting.BooleanSetting;
import com.logvex.logclient.setting.DoubleSetting;
import com.logvex.logclient.setting.EnumSetting;
import com.logvex.logclient.setting.IntSetting;
import com.logvex.logclient.setting.Setting;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;

public class SettingWidget extends Widget {
    private static final int ROW_HEIGHT = 14;

    private final Setting<?> setting;
    private boolean dragging;

    public SettingWidget(Setting<?> setting, int width) {
        this.setting = setting;
        this.width = width;
        this.height = ROW_HEIGHT;
    }

    public Setting<?> getSetting() {
        return setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean hovered = isHovered(mouseX, mouseY);
        if (hovered) {
            context.fill(x, y, x + width, y + height, Theme.WIDGET_HOVER);
        }
        RenderUtil.text(context, setting.getName(), x + 2, y + 3, Theme.TEXT_DIM);

        if (setting instanceof BooleanSetting bool) {
            String label = bool.get() ? "ON" : "OFF";
            RenderUtil.text(context, label, x + width - 2 - RenderUtil.textWidth(label), y + 3,
                    bool.get() ? Theme.ENABLED : Theme.DISABLED);
        } else if (setting instanceof IntSetting integer) {
            renderSlider(context, fraction(integer.get(), integer.getMin(), integer.getMax()), integer.displayValue());
        } else if (setting instanceof DoubleSetting decimal) {
            renderSlider(context, fraction(decimal.get(), decimal.getMin(), decimal.getMax()), decimal.displayValue());
        } else if (setting instanceof EnumSetting<?> choice) {
            String label = choice.displayValue();
            RenderUtil.text(context, label, x + width - 2 - RenderUtil.textWidth(label), y + 3, Theme.ACCENT);
        }
    }

    private void renderSlider(DrawContext context, float fraction, String label) {
        int trackY = y + height - 2;
        context.fill(x + 2, trackY, x + width - 2, trackY + 1, Theme.PANEL_BORDER);
        int filled = Math.round((width - 4) * fraction);
        context.fill(x + 2, trackY, x + 2 + filled, trackY + 1, Theme.ACCENT);
        RenderUtil.text(context, label, x + width - 2 - RenderUtil.textWidth(label), y + 1, Theme.TEXT);
    }

    private float fraction(double value, double min, double max) {
        if (max <= min) {
            return 0f;
        }
        return (float) Math.max(0, Math.min(1, (value - min) / (max - min)));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isHovered(mouseX, mouseY)) {
            return false;
        }
        if (setting instanceof BooleanSetting bool) {
            bool.toggle();
        } else if (setting instanceof EnumSetting<?> choice) {
            cycle(choice, button);
        } else if (setting instanceof IntSetting || setting instanceof DoubleSetting) {
            dragging = true;
            updateSlider(mouseX);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private <E extends Enum<E>> void cycle(EnumSetting<E> choice, int button) {
        if (button == 1) {
            choice.previous();
        } else {
            choice.next();
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            updateSlider(mouseX);
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }

    private void updateSlider(double mouseX) {
        float fraction = (float) Math.max(0, Math.min(1, (mouseX - (x + 2)) / (width - 4)));
        if (setting instanceof IntSetting integer) {
            int value = (int) Math.round(integer.getMin() + fraction * (integer.getMax() - integer.getMin()));
            integer.set(value);
        } else if (setting instanceof DoubleSetting decimal) {
            double value = decimal.getMin() + fraction * (decimal.getMax() - decimal.getMin());
            decimal.set(value);
        }
    }
}
