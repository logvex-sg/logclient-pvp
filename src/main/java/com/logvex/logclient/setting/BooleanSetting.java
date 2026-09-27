package com.logvex.logclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description, defaultValue);
    }

    public boolean get() {
        return value;
    }

    public void toggle() {
        setValue(!value);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromJson(JsonElement json) {
        if (json != null && json.isJsonPrimitive()) {
            value = json.getAsBoolean();
        }
    }

    @Override
    public String displayValue() {
        return value ? "ON" : "OFF";
    }
}
