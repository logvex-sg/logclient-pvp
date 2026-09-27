package com.logvex.logclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class IntSetting extends Setting<Integer> {
    private final int min;
    private final int max;
    private final int step;

    public IntSetting(String name, String description, int defaultValue, int min, int max) {
        this(name, description, defaultValue, min, max, 1);
    }

    public IntSetting(String name, String description, int defaultValue, int min, int max, int step) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.step = Math.max(1, step);
    }

    public int get() {
        return value;
    }

    public void set(int newValue) {
        setValue(Math.max(min, Math.min(max, newValue)));
    }

    public void increment(int amount) {
        set(value + amount * step);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getStep() {
        return step;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromJson(JsonElement json) {
        if (json != null && json.isJsonPrimitive()) {
            set(json.getAsInt());
        }
    }

    @Override
    public String displayValue() {
        return String.valueOf(value);
    }
}
