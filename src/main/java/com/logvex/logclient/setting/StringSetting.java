package com.logvex.logclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class StringSetting extends Setting<String> {
    public StringSetting(String name, String description, String defaultValue) {
        super(name, description, defaultValue);
    }

    public String get() {
        return value;
    }

    public void set(String newValue) {
        setValue(newValue == null ? "" : newValue);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromJson(JsonElement json) {
        if (json != null && json.isJsonPrimitive()) {
            value = json.getAsString();
        }
    }

    @Override
    public String displayValue() {
        return value;
    }
}
