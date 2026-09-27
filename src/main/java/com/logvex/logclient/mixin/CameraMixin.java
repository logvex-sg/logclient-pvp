package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.combat.KillAuraModule;
import com.logvex.logclient.module.combat.ReachModule;
import com.logvex.logclient.module.visual.ViewClipModule;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {
    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void logclient$viewClip(float desiredCameraDistance, CallbackInfoReturnable<Float> cir) {
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return;
        }
        ViewClipModule module = client.getModuleManager().getModule(ViewClipModule.class);
        if (module != null && module.isEnabled()) {
            cir.setReturnValue(desiredCameraDistance);
        }
    }
}
