package com.logvex.logclient.module.player;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoToolModule extends Module {
    public AutoToolModule() {
        super("AutoTool", "Selects the fastest tool when mining", Category.PLAYER, 0);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.currentScreen != null) {
            return;
        }
        if (!mc.options.attackKey.isPressed() || !(mc.crosshairTarget instanceof BlockHitResult hit)
                || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            return;
        }
        BlockPos pos = hit.getBlockPos();
        var state = mc.world.getBlockState(pos);
        if (state.isAir()) {
            return;
        }
        int bestSlot = -1;
        float bestSpeed = 1f;
        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (stack.isEmpty()) {
                continue;
            }
            float speed = stack.getMiningSpeedMultiplier(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = slot;
            }
        }
        if (bestSlot >= 0 && mc.player.getInventory().getSelectedSlot() != bestSlot) {
            mc.player.getInventory().setSelectedSlot(bestSlot);
        }
    }
}
