package com.logvex.logclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class DoubleSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;

    public DoubleSetting(String name, String description, double defaultValue, double min, double max) {
        this(name, description, defaultValue, min, max, 0.05);
    }

    public DoubleSetting(String name, String description, double defaultValue, double min, double max, double step) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double get() {
        return value;
    }

    public float getFloat() {
        return value.floatValue();
    }

    public void set(double newValue) {
        setValue(Math.max(min, Math.min(max, newValue)));
    }

    public void increment(double amount) {
        set(value + amount * step);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }

    @Override
    public void fromJson(JsonElement json) {
        if (json != null && json.isJsonPrimitive()) {
            set(json.getAsDouble());
        }
    }

    @Override
    public String displayValue() {
        return String.format("%.2f", value);
    }
}
