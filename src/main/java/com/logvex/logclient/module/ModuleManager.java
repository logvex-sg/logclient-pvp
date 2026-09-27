package com.logvex.logclient.module;

import com.logvex.logclient.LogClient;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private final Map<Category, List<Module>> byCategory = new EnumMap<>(Category.class);

    public ModuleManager() {
        for (Category category : Category.values()) {
            byCategory.put(category, new ArrayList<>());
        }
    }

    public void register(Module module) {
        modules.add(module);
        byCategory.get(module.getCategory()).add(module);
        LogClient.LOGGER.debug("Registered module {}", module.getName());
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModules(Category category) {
        return byCategory.get(category);
    }

    public Module getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> type) {
        for (Module module : modules) {
            if (type.isInstance(module)) {
                return (T) module;
            }
        }
        return null;
    }

    public void tick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }

    public void tickClient() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onClientTick();
            }
        }
    }

    public void onKeyPressed(int keyCode) {
        if (keyCode == 0) {
            return;
        }
        for (Module module : modules) {
            if (module.getKeybind() == keyCode) {
                if (module.isToggleable()) {
                    module.toggle();
                } else {
                    module.onKeybind();
                }
            }
        }
    }

    public List<Module> getEnabled() {
        List<Module> enabled = new ArrayList<>();
        for (Module module : modules) {
            if (module.isEnabled()) {
                enabled.add(module);
            }
        }
        return enabled;
    }
}
