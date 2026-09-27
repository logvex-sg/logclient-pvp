package com.logvex.logclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class EnumSetting<E extends Enum<E>> extends Setting<E> {
    private final E[] values;

    public EnumSetting(String name, String description, E defaultValue) {
        super(name, description, defaultValue);
        this.values = defaultValue.getDeclaringClass().getEnumConstants();
    }

    public E[] getValues() {
        return values;
    }

    public void next() {
        setValue(values[(value.ordinal() + 1) % values.length]);
    }

    public void previous() {
        setValue(values[(value.ordinal() - 1 + values.length) % values.length]);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value.name());
    }

    @Override
    public void fromJson(JsonElement json) {
        if (json == null || !json.isJsonPrimitive()) {
            return;
        }
        String name = json.getAsString();
        for (E candidate : values) {
            if (candidate.name().equalsIgnoreCase(name)) {
                value = candidate;
                return;
            }
        }
    }

    @Override
    public String displayValue() {
        String raw = value.name();
        StringBuilder sb = new StringBuilder(raw.length());
        boolean upper = true;
        for (char c : raw.toCharArray()) {
            if (c == '_') {
                sb.append(' ');
                upper = true;
            } else {
                sb.append(upper ? Character.toUpperCase(c) : Character.toLowerCase(c));
                upper = false;
            }
        }
        return sb.toString();
    }
}
