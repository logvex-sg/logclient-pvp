package com.logvex.logclient.module.combat;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;
import com.logvex.logclient.setting.EnumSetting;
import com.logvex.logclient.setting.IntSetting;
import com.logvex.logclient.util.Stopwatch;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Random;

public class AutoClickerModule extends Module {
    public enum Mode {
        CPS, HOLD
    }

    private final EnumSetting<Mode> mode = choice("Mode", "Trigger style", Mode.CPS);
    private final IntSetting minCps = integer("MinCPS", "Minimum clicks per second", 8, 1, 20);
    private final IntSetting maxCps = integer("MaxCPS", "Maximum clicks per second", 14, 1, 20);
    private final BooleanSetting onlyWeapon = bool("OnlyWeapon", "Only click while holding a weapon", true);
    private final BooleanSetting requireTarget = bool("RequireTarget", "Only click when aiming at an entity", false);
    private final BooleanSetting jitter = bool("Jitter", "Randomise click intervals", true);

    private final Stopwatch timer = new Stopwatch();
    private final Random random = new Random();
    private long nextDelay;

    public AutoClickerModule() {
        super("AutoClicker", "Clicks automatically while the attack key is held", Category.COMBAT, 0);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.currentScreen != null) {
            return;
        }
        if (mode.getValue() == Mode.HOLD && !mc.options.attackKey.isPressed()) {
            return;
        }
        if (onlyWeapon.get() && !isHoldingWeapon(mc)) {
            return;
        }
        if (requireTarget.get()) {
            Entity target = mc.targetedEntity;
            if (!(target instanceof LivingEntity)) {
                return;
            }
        }
        if (mc.player.getAttackCooldownProgress(0f) < 1f) {
            return;
        }
        if (nextDelay == 0) {
            nextDelay = computeDelay();
        }
        if (!timer.hasElapsed(nextDelay, true)) {
            return;
        }
        nextDelay = computeDelay();
        if (mc.targetedEntity instanceof LivingEntity living) {
            mc.interactionManager.attackEntity(mc.player, living);
        } else {
            mc.interactionManager.attackEntity(mc.player, null);
        }
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private boolean isHoldingWeapon(MinecraftClient mc) {
        String name = mc.player.getMainHandStack().getItem().toString().toLowerCase();
        return name.contains("sword") || name.contains("axe") || name.contains("trident") || name.contains("mace");
    }

    private long computeDelay() {
        int low = Math.min(minCps.get(), maxCps.get());
        int high = Math.max(minCps.get(), maxCps.get());
        int cps = jitter.get() ? low + random.nextInt(high - low + 1) : high;
        return Math.max(1, 1000L / cps);
    }
}
