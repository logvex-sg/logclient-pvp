package com.logvex.logclient.module;

import com.logvex.logclient.setting.BooleanSetting;
import com.logvex.logclient.setting.DoubleSetting;
import com.logvex.logclient.setting.EnumSetting;
import com.logvex.logclient.setting.IntSetting;
import com.logvex.logclient.setting.Setting;
import com.logvex.logclient.setting.StringSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();
    private final BooleanSetting enabledSetting;
    private boolean enabled;
    private int keybind;
    private boolean expanded;

    protected Module(String name, String description, Category category, int defaultKey) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keybind = defaultKey;
        this.enabledSetting = new BooleanSetting("Enabled", "Toggles the module", false);
        this.enabledSetting.onChange(this::applyEnabled);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public BooleanSetting getEnabledSetting() {
        return enabledSetting;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /** Action modules run their keybind without changing their enabled state. */
    public boolean isToggleable() {
        return true;
    }

    public void onKeybind() {
    }

    public void toggle() {
        enabledSetting.setValue(!enabled);
    }

    public void setEnabled(boolean value) {
        enabledSetting.setValue(value);
    }

    private void applyEnabled(boolean value) {
        if (this.enabled == value) {
            return;
        }
        this.enabled = value;
        if (value) {
            onEnable();
        } else {
            onDisable();
        }
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    /** Runs only while a world is loaded. */
    public void onTick() {
    }

    /** Runs every client tick, including while no world is loaded (menus, disconnect screens). */
    public void onClientTick() {
    }

    protected <S extends Setting<?>> S register(S setting) {
        settings.add(setting);
        return setting;
    }

    protected BooleanSetting bool(String name, String description, boolean defaultValue) {
        return register(new BooleanSetting(name, description, defaultValue));
    }

    protected IntSetting integer(String name, String description, int defaultValue, int min, int max) {
        return register(new IntSetting(name, description, defaultValue, min, max));
    }

    protected IntSetting integer(String name, String description, int defaultValue, int min, int max, int step) {
        return register(new IntSetting(name, description, defaultValue, min, max, step));
    }

    protected DoubleSetting decimal(String name, String description, double defaultValue, double min, double max) {
        return register(new DoubleSetting(name, description, defaultValue, min, max));
    }

    protected StringSetting text(String name, String description, String defaultValue) {
        return register(new StringSetting(name, description, defaultValue));
    }

    protected <E extends Enum<E>> EnumSetting<E> choice(String name, String description, E defaultValue) {
        return register(new EnumSetting<>(name, description, defaultValue));
    }

    public List<Setting<?>> getSettings() {
        return Collections.unmodifiableList(settings);
    }

    public Setting<?> getSetting(String settingName) {
        for (Setting<?> setting : settings) {
            if (setting.getName().equalsIgnoreCase(settingName)) {
                return setting;
            }
        }
        return null;
    }

    protected boolean getBoolean(String settingName) {
        Setting<?> setting = getSetting(settingName);
        return setting instanceof BooleanSetting bool && bool.get();
    }

    protected int getInt(String settingName) {
        Setting<?> setting = getSetting(settingName);
        return setting instanceof IntSetting integer ? integer.get() : 0;
    }

    protected double getDouble(String settingName) {
        Setting<?> setting = getSetting(settingName);
        return setting instanceof DoubleSetting decimal ? decimal.get() : 0.0;
    }
}
