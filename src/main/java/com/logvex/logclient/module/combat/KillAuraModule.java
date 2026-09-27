package com.logvex.logclient.module.combat;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.EnumSetting;
import com.logvex.logclient.setting.IntSetting;
import com.logvex.logclient.util.CombatTarget;
import com.logvex.logclient.util.PlayerUtil;
import com.logvex.logclient.util.Stopwatch;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class KillAuraModule extends Module {
    public enum Targets {
        PLAYERS, MOBS, ALL
    }

    public enum Rotation {
        SERVER, CLIENT, NONE
    }

    private final EnumSetting<Targets> targets = choice("Targets", "Which entities to attack", Targets.PLAYERS);
    private final EnumSetting<Rotation> rotation = choice("Rotation", "How to aim at the target", Rotation.SERVER);
    private final IntSetting range = integer("Range", "Attack range in blocks", 3, 1, 6);
    private final IntSetting delay = integer("Delay", "Minimum ticks between attacks", 12, 1, 40);
    private final IntSetting fov = integer("FOV", "Maximum aim angle in degrees", 180, 10, 180);
    private final IntSetting targetSwitchDelay = integer("SwitchDelay", "Ticks before switching target", 4, 0, 40);

    private final Stopwatch attackTimer = new Stopwatch();
    private LivingEntity currentTarget;
    private long switchTime;

    public KillAuraModule() {
        super("KillAura", "Attacks nearby entities automatically", Category.COMBAT, 0);
    }

    @Override
    public void onDisable() {
        currentTarget = null;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) {
            return;
        }
        LivingEntity target = selectTarget(mc);
        currentTarget = target;
        CombatTarget.set(target);
        if (target == null) {
            return;
        }
        if (rotation.getValue() != Rotation.NONE) {
            float yaw = PlayerUtil.yawTo(mc.player, target);
            float pitch = PlayerUtil.pitchTo(mc.player, target.getBoundingBox().getCenter());
            if (rotation.getValue() == Rotation.SERVER) {
                mc.player.setYaw(yaw);
                mc.player.setPitch(pitch);
            } else {
                mc.player.setYaw(yaw);
                mc.player.setPitch(pitch);
            }
        }
        if (mc.player.getAttackCooldownProgress(0f) >= 1f && attackTimer.hasElapsed(delay.get() * 50L, true)) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private LivingEntity selectTarget(MinecraftClient mc) {
        if (currentTarget != null && isValid(mc, currentTarget)
                && System.currentTimeMillis() - switchTime < targetSwitchDelay.get() * 50L) {
            return currentTarget;
        }
        LivingEntity best = null;
        double bestDistance = Double.MAX_VALUE;
        double maxRange = range.get() + 0.5;
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity living) || !isTarget(living)) {
                continue;
            }
            double distance = mc.player.squaredDistanceTo(entity);
            if (distance > maxRange * maxRange) {
                continue;
            }
            Vec3d center = living.getBoundingBox().getCenter();
            float angle = Math.abs(PlayerUtil.yawTo(mc.player, center) - mc.player.getYaw());
            angle = Math.abs(((angle + 180f) % 360f) - 180f);
            if (angle > fov.get()) {
                continue;
            }
            if (distance < bestDistance) {
                bestDistance = distance;
                best = living;
            }
        }
        if (best != currentTarget) {
            switchTime = System.currentTimeMillis();
        }
        return best;
    }

    private boolean isValid(MinecraftClient mc, LivingEntity entity) {
        return !entity.isRemoved() && !entity.isDead() && isTarget(entity)
                && mc.player.squaredDistanceTo(entity) <= Math.pow(range.get() + 0.5, 2);
    }

    private boolean isTarget(LivingEntity entity) {
        if (entity == MinecraftClient.getInstance().player || CombatTarget.isFriend(entity.getUuid())) {
            return false;
        }
        return switch (targets.getValue()) {
            case PLAYERS -> entity instanceof PlayerEntity;
            case MOBS -> entity instanceof HostileEntity;
            case ALL -> true;
        };
    }

    public LivingEntity getCurrentTarget() {
        return currentTarget;
    }

    public boolean isActive() {
        return isEnabled() && currentTarget != null;
    }
}
