package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.visual.NoRenderModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    private static final Identifier POWDER_SNOW_OUTLINE =
            Identifier.ofVanilla("textures/misc/powder_snow_outline.png");

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void logclient$noVignette(DrawContext context, Entity entity, CallbackInfo ci) {
        if (enabled(NoRenderModule.VIGNETTE)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void logclient$noPortal(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        if (enabled(NoRenderModule.PORTAL)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderNauseaOverlay", at = @At("HEAD"), cancellable = true)
    private void logclient$noNausea(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        if (enabled(NoRenderModule.NAUSEA)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    private void logclient$noPowderSnow(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
        if (POWDER_SNOW_OUTLINE.equals(texture) && enabled(NoRenderModule.POWDER_SNOW)) {
            ci.cancel();
        }
    }

    private static boolean enabled(String option) {
        LogClient client = LogClient.getInstance();
        return client != null && client.isNoRender(option);
    }
}
