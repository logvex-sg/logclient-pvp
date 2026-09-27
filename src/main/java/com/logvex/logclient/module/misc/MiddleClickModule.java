package com.logvex.logclient.module.misc;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.EnumSetting;
import com.logvex.logclient.setting.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class MiddleClickModule extends Module {
    public enum Action {
        PEARL, FRIEND, FIREWORK
    }

    private final EnumSetting<Action> action = choice("Action", "What to do on middle click", Action.PEARL);
    private final IntSetting cooldown = integer("Cooldown", "Ticks between actions", 10, 1, 100);

    private long lastAction;

    public MiddleClickModule() {
        super("MiddleClick", "Runs an action on middle mouse click", Category.MISC, 0);
    }

    public void onMiddleClick(MinecraftClient mc) {
        if (!isEnabled() || mc.player == null || mc.interactionManager == null) {
            return;
        }
        if (System.currentTimeMillis() - lastAction < cooldown.get() * 50L) {
            return;
        }
        switch (action.getValue()) {
            case PEARL -> useItem(mc, "ender_pearl");
            case FIREWORK -> useItem(mc, "firework_rocket");
            case FRIEND -> {
                if (mc.crosshairTarget instanceof EntityHitResult hit
                        && mc.crosshairTarget.getType() == HitResult.Type.ENTITY
                        && hit.getEntity() instanceof PlayerEntity player) {
                    com.logvex.logclient.util.CombatTarget.toggleFriend(player.getUuid());
                }
            }
        }
        lastAction = System.currentTimeMillis();
    }

    private void useItem(MinecraftClient mc, String itemName) {
        for (int slot = 0; slot < 9; slot++) {
            String name = mc.player.getInventory().getStack(slot).getItem().toString().toLowerCase();
            if (name.contains(itemName)) {
                mc.player.getInventory().setSelectedSlot(slot);
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                return;
            }
        }
    }

    public boolean isFriend(Entity entity) {
        return isEnabled() && action.getValue() == Action.FRIEND
                && entity instanceof LivingEntity && com.logvex.logclient.util.CombatTarget.isFriend(entity.getUuid());
    }
}
