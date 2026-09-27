package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.optimization.NoFogModule;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.LavaFogModifier;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LavaFogModifier.class)
public class LavaFogMixin {
    @Inject(method = "applyStartEndModifier", at = @At("HEAD"), cancellable = true)
    private void logclient$noLavaFog(FogData data, Camera camera, ClientWorld world, float tickProgress,
                                     RenderTickCounter tickCounter, CallbackInfo ci) {
        NoFogModule fog = LogClient.noFog();
        if (fog != null && fog.isLavaFog()) {
            ci.cancel();
        }
    }
}
