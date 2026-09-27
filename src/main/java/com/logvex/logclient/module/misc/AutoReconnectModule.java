package com.logvex.logclient.module.misc;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.CookieStorage;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class AutoReconnectModule extends Module {
    private final IntSetting delaySeconds = integer("Delay", "Seconds before reconnecting", 5, 1, 60);

    private long disconnectTime = -1;

    public AutoReconnectModule() {
        super("AutoReconnect", "Reconnects automatically after being kicked", Category.MISC, 0);
    }

    @Override
    public void onDisable() {
        disconnectTime = -1;
    }

    @Override
    public void onClientTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen instanceof DisconnectedScreen) {
            if (disconnectTime < 0) {
                disconnectTime = System.currentTimeMillis();
            }
            if (shouldReconnect(mc)) {
                reconnect(mc);
            }
        } else {
            disconnectTime = -1;
        }
    }

    public boolean shouldReconnect(MinecraftClient mc) {
        if (!isEnabled() || disconnectTime < 0) {
            return false;
        }
        return System.currentTimeMillis() - disconnectTime >= delaySeconds.get() * 1000L;
    }

    public void reconnect(MinecraftClient mc) {
        ServerInfo entry = mc.getCurrentServerEntry();
        if (entry == null) {
            return;
        }
        disconnectTime = -1;
        ConnectScreen.connect(new TitleScreen(), mc, ServerAddress.parse(entry.address), entry, false,
                new CookieStorage(java.util.Map.of(), java.util.Map.of(), false));
    }
}
