package com.logvex.logclient.gui.widget;

import com.logvex.logclient.gui.Theme;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.Setting;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Widget {
    private static final int ROW_HEIGHT = 16;

    private final Module module;
    private final List<SettingWidget> settingWidgets = new ArrayList<>();
    private boolean binding;

    public ModuleButton(Module module, int width) {
        this.module = module;
        this.width = width;
        this.height = ROW_HEIGHT;
        for (Setting<?> setting : module.getSettings()) {
            if (setting == module.getEnabledSetting()) {
                continue;
            }
            settingWidgets.add(new SettingWidget(setting, width - 8));
        }
    }

    public Module getModule() {
        return module;
    }

    public void layout() {
        int offset = ROW_HEIGHT + 2;
        for (SettingWidget widget : settingWidgets) {
            widget.setPosition(x + 4, y + offset);
            offset += widget.getHeight() + 2;
        }
        height = module.isExpanded() ? offset : ROW_HEIGHT;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean hovered = isHovered(mouseX, mouseY);
        int background = module.isEnabled() ? Theme.WIDGET_ACTIVE : (hovered ? Theme.WIDGET_HOVER : 0);
        if (background != 0) {
            context.fill(x, y, x + width, y + ROW_HEIGHT, background);
        }
        int color = module.isEnabled() ? Theme.ENABLED : Theme.TEXT;
        RenderUtil.text(context, module.getName(), x + 4, y + 4, color);

        String keybind = binding ? "..." : keyName(module.getKeybind());
        RenderUtil.text(context, keybind, x + width - 4 - RenderUtil.textWidth(keybind), y + 4, Theme.TEXT_DIM);

        if (module.isExpanded()) {
            for (SettingWidget widget : settingWidgets) {
                widget.render(context, mouseX, mouseY, delta);
            }
        }
    }

    private String keyName(int keyCode) {
        if (keyCode == 0) {
            return "";
        }
        String name = org.lwjgl.glfw.GLFW.glfwGetKeyName(keyCode, 0);
        return name == null ? "KEY" + keyCode : name.toUpperCase();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (module.isExpanded() && module.isExpanded()) {
            for (SettingWidget widget : settingWidgets) {
                if (widget.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        if (isHovered(mouseX, mouseY)) {
            switch (button) {
                case 0 -> module.toggle();
                case 1 -> module.setExpanded(!module.isExpanded());
                case 2 -> binding = true;
                default -> {
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (SettingWidget widget : settingWidgets) {
            if (widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        for (SettingWidget widget : settingWidgets) {
            widget.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (binding) {
            module.setKeybind(keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE ? 0 : keyCode);
            binding = false;
            return true;
        }
        return false;
    }

    public boolean isBinding() {
        return binding;
    }

    public int getContentHeight() {
        layout();
        return height;
    }

    public static int rowHeight() {
        return ROW_HEIGHT;
    }

    public MinecraftClient client() {
        return MinecraftClient.getInstance();
    }
}
