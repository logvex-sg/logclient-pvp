package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import net.minecraft.client.Keyboard;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void logclient$moduleKeybinds(long window, int action, KeyInput input, CallbackInfo ci) {
        if (action != 1) {
            return;
        }
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return;
        }
        if (client.mc().currentScreen == null) {
            client.onKeyPressed(input.key());
        }
    }
}
