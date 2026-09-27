package com.logvex.logclient.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("logclient.json");

    private ConfigManager() {
    }

    public static void save() {
        JsonObject root = new JsonObject();
        root.addProperty("version", LogClient.VERSION);

        JsonObject modules = new JsonObject();
        for (Module module : LogClient.getInstance().getModuleManager().getModules()) {
            JsonObject moduleJson = new JsonObject();
            moduleJson.addProperty("enabled", module.isEnabled());
            moduleJson.addProperty("keybind", module.getKeybind());
            JsonObject settings = new JsonObject();
            for (Setting<?> setting : module.getSettings()) {
                settings.add(setting.getName(), setting.toJson());
            }
            moduleJson.add("settings", settings);
            modules.add(module.getName(), moduleJson);
        }
        root.add("modules", modules);

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, LogClient.GSON.toJson(root));
        } catch (IOException exception) {
            LogClient.LOGGER.error("Failed to save config", exception);
        }
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            return;
        }
        try {
            String content = Files.readString(CONFIG_PATH);
            JsonObject root = JsonParser.parseString(content).getAsJsonObject();
            if (!root.has("modules")) {
                return;
            }
            JsonObject modules = root.getAsJsonObject("modules");
            for (Module module : LogClient.getInstance().getModuleManager().getModules()) {
                if (!modules.has(module.getName())) {
                    continue;
                }
                JsonObject moduleJson = modules.getAsJsonObject(module.getName());
                if (moduleJson.has("keybind")) {
                    module.setKeybind(moduleJson.get("keybind").getAsInt());
                }
                if (moduleJson.has("settings")) {
                    JsonObject settings = moduleJson.getAsJsonObject("settings");
                    for (Setting<?> setting : module.getSettings()) {
                        if (settings.has(setting.getName())) {
                            setting.fromJson(settings.get(setting.getName()));
                        }
                    }
                }
                if (moduleJson.has("enabled") && moduleJson.get("enabled").getAsBoolean()) {
                    module.setEnabled(true);
                }
            }
        } catch (Exception exception) {
            LogClient.LOGGER.error("Failed to load config", exception);
        }
    }
}
