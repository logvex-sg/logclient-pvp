package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.optimization.NoFogModule;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.BlindnessEffectFogModifier;
import net.minecraft.client.render.fog.DarknessEffectFogModifier;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Blindness and darkness share a base class but are distinct targets, so each gets its own
 * injection guarded by the same toggle.
 */
public class StatusEffectFogMixin {
    @Mixin(BlindnessEffectFogModifier.class)
    public static class Blindness {
        @Inject(method = "applyStartEndModifier", at = @At("HEAD"), cancellable = true)
        private void logclient$noBlindnessFog(FogData data, Camera camera, ClientWorld world, float tickProgress,
                                              RenderTickCounter tickCounter, CallbackInfo ci) {
            NoFogModule fog = LogClient.noFog();
            if (fog != null && fog.isBlindness()) {
                ci.cancel();
            }
        }
    }

    @Mixin(DarknessEffectFogModifier.class)
    public static class Darkness {
        @Inject(method = "applyStartEndModifier", at = @At("HEAD"), cancellable = true)
        private void logclient$noDarknessFog(FogData data, Camera camera, ClientWorld world, float tickProgress,
                                             RenderTickCounter tickCounter, CallbackInfo ci) {
            NoFogModule fog = LogClient.noFog();
            if (fog != null && fog.isBlindness()) {
                ci.cancel();
            }
        }
    }
}
