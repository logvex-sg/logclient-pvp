package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.optimization.FpsBoostModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void logclient$noWeather(CallbackInfo ci) {
        LogClient client = LogClient.getInstance();
        if (client != null && client.isNoWeather()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void logclient$noClouds(CallbackInfo ci) {
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return;
        }
        FpsBoostModule boost = client.getModuleManager().getModule(FpsBoostModule.class);
        if (boost != null && boost.isEnabled() && boost.isNoClouds()) {
            ci.cancel();
        }
    }
}
