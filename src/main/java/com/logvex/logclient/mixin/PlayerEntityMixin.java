package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.player.SafeWalkModule;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "clipAtLedge", at = @At("HEAD"), cancellable = true)
    private void logclient$safeWalk(CallbackInfoReturnable<Boolean> cir) {
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return;
        }
        SafeWalkModule safeWalk = client.getModuleManager().getModule(SafeWalkModule.class);
        if (safeWalk != null && safeWalk.shouldHold(client.mc())) {
            cir.setReturnValue(true);
        }
    }
}
