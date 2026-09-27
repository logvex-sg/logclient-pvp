package com.logvex.logclient.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class PlayerUtil {
    private PlayerUtil() {
    }

    public static int ping(AbstractClientPlayerEntity player) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getNetworkHandler() == null) {
            return 0;
        }
        PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return entry == null ? 0 : entry.getLatency();
    }

    public static float health(LivingEntity entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }

    public static float healthFraction(LivingEntity entity) {
        float max = entity.getMaxHealth();
        return max <= 0 ? 0f : Math.max(0f, Math.min(1f, health(entity) / max));
    }

    public static double distance(Entity from, Entity to) {
        return Math.sqrt(from.squaredDistanceTo(to));
    }

    public static double distanceToCamera(Entity entity) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Vec3d camera = mc.gameRenderer.getCamera().getCameraPos();
        return Math.sqrt(camera.squaredDistanceTo(entity.getX(), entity.getY(), entity.getZ()));
    }

    public static String direction(float yaw) {
        float normalized = MathHelper.wrapDegrees(yaw);
        if (normalized >= -45 && normalized < 45) {
            return "S";
        }
        if (normalized >= 45 && normalized < 135) {
            return "W";
        }
        if (normalized >= -135 && normalized < -45) {
            return "E";
        }
        return "N";
    }

    public static float yawTo(Entity from, Entity to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        return (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
    }

    public static float yawTo(Entity from, Vec3d target) {
        double dx = target.x - from.getX();
        double dz = target.z - from.getZ();
        return (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
    }

    public static float pitchTo(Entity from, Vec3d target) {
        double dx = target.x - from.getX();
        double dy = target.y - from.getEyeY();
        double dz = target.z - from.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        return (float) -Math.toDegrees(Math.atan2(dy, horizontal));
    }

    public static boolean isSameTeam(AbstractClientPlayerEntity self, AbstractClientPlayerEntity other) {
        return self.isTeammate(other);
    }

    public static String simpleName(String name) {
        int index = name.indexOf('\u00a7');
        return index < 0 ? name : name.substring(0, index);
    }

    public static int armorValue(LivingEntity entity) {
        return entity.getArmor();
    }

    public static float durabilityFraction(net.minecraft.item.ItemStack stack) {
        if (!stack.isDamageable()) {
            return 1f;
        }
        int max = stack.getMaxDamage();
        if (max <= 0) {
            return 1f;
        }
        return 1f - (float) stack.getDamage() / (float) max;
    }
}
