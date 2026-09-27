package com.logvex.logclient.setting;

import com.google.gson.JsonElement;

import java.util.function.Consumer;

public abstract class Setting<T> {
    private final String name;
    private final String description;
    protected T value;
    private Consumer<T> onChange = v -> {
    };

    protected Setting(String name, String description, T defaultValue) {
        this.name = name;
        this.description = description;
        this.value = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T newValue) {
        this.value = newValue;
        onChange.accept(newValue);
    }

    public Setting<T> onChange(Consumer<T> listener) {
        this.onChange = listener;
        return this;
    }

    public abstract JsonElement toJson();

    public abstract void fromJson(JsonElement json);

    public abstract String displayValue();
}
