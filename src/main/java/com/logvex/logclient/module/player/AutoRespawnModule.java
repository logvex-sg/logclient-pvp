package com.logvex.logclient.module.player;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;
import com.logvex.logclient.setting.IntSetting;
import net.minecraft.client.MinecraftClient;

public class AutoRespawnModule extends Module {
    private final IntSetting delay = integer("Delay", "Ticks before respawning", 10, 0, 100);
    private final BooleanSetting announce = bool("Announce", "Print the death position to chat", false);

    private int ticks;

    public AutoRespawnModule() {
        super("AutoRespawn", "Respawns automatically after dying", Category.PLAYER, 0);
    }

    @Override
    public void onDisable() {
        ticks = 0;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || !(mc.currentScreen instanceof net.minecraft.client.gui.screen.DeathScreen)) {
            ticks = 0;
            return;
        }
        ticks++;
        if (ticks < delay.get()) {
            return;
        }
        ticks = 0;
        if (announce.get()) {
            mc.player.networkHandler.sendChatMessage(String.format("I died at %.0f %.0f %.0f",
                    mc.player.getX(), mc.player.getY(), mc.player.getZ()));
        }
        mc.player.requestRespawn();
        mc.setScreen(null);
    }
}
