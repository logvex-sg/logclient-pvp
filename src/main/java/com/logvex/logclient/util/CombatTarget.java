package com.logvex.logclient.util;

import net.minecraft.entity.LivingEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class CombatTarget {
    private static final Set<UUID> FRIENDS = new HashSet<>();
    private static LivingEntity target;
    private static long lastUpdate;

    private CombatTarget() {
    }

    public static void set(LivingEntity entity) {
        target = entity;
        lastUpdate = System.currentTimeMillis();
    }

    public static LivingEntity get() {
        if (target != null && (target.isRemoved() || target.isDead())) {
            return null;
        }
        return target;
    }

    public static long lastUpdate() {
        return lastUpdate;
    }

    public static void clear() {
        target = null;
    }

    public static void toggleFriend(UUID uuid) {
        if (!FRIENDS.remove(uuid)) {
            FRIENDS.add(uuid);
        }
    }

    public static boolean isFriend(UUID uuid) {
        return FRIENDS.contains(uuid);
    }

    public static Set<UUID> getFriends() {
        return Collections.unmodifiableSet(FRIENDS);
    }
}
