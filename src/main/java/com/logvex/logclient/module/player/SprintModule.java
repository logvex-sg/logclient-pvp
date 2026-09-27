package com.logvex.logclient.module.player;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;
import com.logvex.logclient.setting.EnumSetting;
import net.minecraft.client.MinecraftClient;

public class SprintModule extends Module {
    public enum Mode {
        LEGIT, ALWAYS
    }

    private final EnumSetting<Mode> mode = choice("Mode", "Sprint trigger style", Mode.LEGIT);
    private final BooleanSetting keepSprint = bool("KeepSprint", "Keep sprinting while using items", false);

    public SprintModule() {
        super("Sprint", "Automatically sprints for you", Category.PLAYER, 0);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.currentScreen != null) {
            return;
        }
        if (mc.player.isSneaking() || mc.player.isUsingItem() && !keepSprint.get()) {
            return;
        }
        boolean moving = mc.options.forwardKey.isPressed()
                && !mc.options.backKey.isPressed()
                && !mc.player.isTouchingWater()
                && mc.player.getHungerManager().getFoodLevel() > 6;
        if (mode.getValue() == Mode.ALWAYS ? moving : moving && mc.options.forwardKey.isPressed()) {
            mc.player.setSprinting(true);
        }
    }
}
