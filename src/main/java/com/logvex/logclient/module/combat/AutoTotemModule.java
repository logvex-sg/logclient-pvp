package com.logvex.logclient.module.combat;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.IntSetting;
import com.logvex.logclient.util.Stopwatch;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotemModule extends Module {
    private final IntSetting delay = integer("Delay", "Ticks between swaps", 5, 1, 40);
    private final IntSetting healthThreshold = integer("Health", "Swap below this health", 14, 1, 36);

    private final Stopwatch timer = new Stopwatch();

    public AutoTotemModule() {
        super("AutoTotem", "Moves a totem into your offhand when low", Category.COMBAT, 0);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.currentScreen != null || mc.interactionManager == null) {
            return;
        }
        if (mc.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
            return;
        }
        if (mc.player.getHealth() > healthThreshold.get()) {
            return;
        }
        if (!timer.hasElapsed(delay.get() * 50L, true)) {
            return;
        }
        int slot = mc.player.getInventory().getSlotWithStack(Items.TOTEM_OF_UNDYING.getDefaultStack());
        if (slot < 0 || slot > 8) {
            return;
        }
        int syncId = mc.player.currentScreenHandler.syncId;
        mc.interactionManager.clickSlot(syncId, slot + 36, 40, SlotActionType.SWAP, mc.player);
    }
}
