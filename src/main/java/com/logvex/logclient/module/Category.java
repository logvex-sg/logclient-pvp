package com.logvex.logclient.module;

public enum Category {
    COMBAT("Combat"),
    HUD("HUD"),
    VISUAL("Visual"),
    PLAYER("Player"),
    OPTIMIZATION("Optimization"),
    MISC("Misc");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
