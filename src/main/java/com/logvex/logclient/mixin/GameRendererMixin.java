package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.visual.NoHurtCamModule;
import com.logvex.logclient.module.visual.ZoomModule;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void logclient$noHurtTilt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return;
        }
        NoHurtCamModule module = client.getModuleManager().getModule(NoHurtCamModule.class);
        if (module != null && module.isEnabled() && module.isDisableTilt()) {
            ci.cancel();
        }
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void logclient$noBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return;
        }
        NoHurtCamModule module = client.getModuleManager().getModule(NoHurtCamModule.class);
        if (module != null && module.isEnabled() && module.isDisableBob()) {
            ci.cancel();
        }
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void logclient$zoom(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> cir) {
        LogClient client = LogClient.getInstance();
        if (client != null && client.isZooming()) {
            cir.setReturnValue(cir.getReturnValueF() / client.zoomDivisor());
        }
    }

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void logclient$noTotemAnimation(ItemStack stack, CallbackInfo ci) {
        LogClient client = LogClient.getInstance();
        if (client != null && client.isNoRender(com.logvex.logclient.module.visual.NoRenderModule.TOTEM)) {
            ci.cancel();
        }
    }
}
