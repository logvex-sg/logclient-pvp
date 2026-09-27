package com.logvex.logclient.gui;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.gui.widget.ModuleButton;
import com.logvex.logclient.util.RenderUtil;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ClickGuiScreen extends Screen {
    private static final int PANEL_WIDTH = 120;
    private static final int PANEL_GAP = 6;
    private static final int HEADER_HEIGHT = 18;

    private final Map<Category, List<ModuleButton>> panels = new EnumMap<>(Category.class);
    private final List<Category> ordered = new ArrayList<>();
    private Category draggingPanel;
    private int dragOffsetX;
    private int dragOffsetY;
    private double scroll;

    public ClickGuiScreen() {
        super(Text.literal("LogClient"));
    }

    @Override
    protected void init() {
        panels.clear();
        ordered.clear();
        int panelX = 10;
        for (Category category : Category.values()) {
            List<Module> modules = LogClient.getInstance().getModuleManager().getModules(category);
            if (modules.isEmpty()) {
                continue;
            }
            List<ModuleButton> buttons = new ArrayList<>();
            for (Module module : modules) {
                buttons.add(new ModuleButton(module, PANEL_WIDTH - 8));
            }
            panels.put(category, buttons);
            ordered.add(category);
            panelX += PANEL_WIDTH + PANEL_GAP;
        }
        layoutPanels();
    }

    private void layoutPanels() {
        int panelX = 10;
        for (Category category : ordered) {
            List<ModuleButton> buttons = panels.get(category);
            int buttonY = 10 + HEADER_HEIGHT + 4 - (int) scroll;
            for (ModuleButton button : buttons) {
                button.setPosition(panelX + 4, buttonY);
                button.layout();
                buttonY += button.getContentHeight() + 2;
            }
            panelX += PANEL_WIDTH + PANEL_GAP;
        }
    }

    private int panelHeight(Category category) {
        int height = HEADER_HEIGHT + 8;
        for (ModuleButton button : panels.get(category)) {
            height += button.getContentHeight() + 2;
        }
        return height;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        int panelX = 10;
        for (Category category : ordered) {
            int height = panelHeight(category);
            context.fill(panelX, 10, panelX + PANEL_WIDTH, 10 + height, Theme.PANEL);
            RenderUtil.outline(context, panelX, 10, PANEL_WIDTH, height, 1, Theme.PANEL_BORDER);
            context.fill(panelX, 10, panelX + PANEL_WIDTH, 10 + HEADER_HEIGHT, Theme.PANEL_HEADER);
            RenderUtil.text(context, category.getDisplayName(), panelX + 6, 10 + 5, Theme.ACCENT);

            RenderUtil.scissor(context, panelX, 10 + HEADER_HEIGHT, PANEL_WIDTH, height - HEADER_HEIGHT);
            for (ModuleButton button : panels.get(category)) {
                button.render(context, mouseX, mouseY, delta);
            }
            RenderUtil.resetScissor(context);

            panelX += PANEL_WIDTH + PANEL_GAP;
        }

        RenderUtil.text(context, "Right Shift to toggle \u2022 LMB toggle \u2022 RMB expand \u2022 MMB bind",
                6, height - 12, Theme.TEXT_DIM);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();

        int panelX = 10;
        for (Category category : ordered) {
            int height = panelHeight(category);
            if (mouseX >= panelX && mouseX <= panelX + PANEL_WIDTH && mouseY >= 10 && mouseY <= 10 + HEADER_HEIGHT) {
                draggingPanel = category;
                dragOffsetX = (int) mouseX - panelX;
                dragOffsetY = (int) mouseY - 10;
                return true;
            }
            for (ModuleButton moduleButton : panels.get(category)) {
                if (moduleButton.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
            panelX += PANEL_WIDTH + PANEL_GAP;
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (draggingPanel != null) {
            scroll = Math.max(0, scroll - offsetY);
            layoutPanels();
            return true;
        }
        for (List<ModuleButton> buttons : panels.values()) {
            for (ModuleButton button : buttons) {
                if (button.mouseDragged(click.x(), click.y(), click.button(), offsetX, offsetY)) {
                    return true;
                }
            }
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        draggingPanel = null;
        for (List<ModuleButton> buttons : panels.values()) {
            for (ModuleButton button : buttons) {
                button.mouseReleased(click.x(), click.y(), click.button());
            }
        }
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scroll = Math.max(0, scroll - verticalAmount * 12);
        layoutPanels();
        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        int keyCode = input.getKeycode();
        for (List<ModuleButton> buttons : panels.values()) {
            for (ModuleButton button : buttons) {
                if (button.keyPressed(keyCode, input.scancode(), input.modifiers())) {
                    return true;
                }
            }
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT || keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public void close() {
        super.close();
        com.logvex.logclient.config.ConfigManager.save();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
