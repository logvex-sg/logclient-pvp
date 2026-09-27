package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void logclient$middleClick(long window, MouseInput input, int action, CallbackInfo ci) {
        if (action != 1) {
            return;
        }
        LogClient client = LogClient.getInstance();
        if (client == null || client.mc().currentScreen != null) {
            return;
        }
        client.onMousePressed(input.button());
    }
}
