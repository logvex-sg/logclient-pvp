package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.player.FastPlaceModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla stamps a 4-tick cooldown right after a successful use, which is what limits block
 * placement speed. Rewriting that write is enough; the per-tick decrement is left alone.
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private int itemUseCooldown;

    @Inject(method = "doItemUse", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/MinecraftClient;itemUseCooldown:I",
            opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void logclient$fastPlace(CallbackInfo ci) {
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return;
        }
        FastPlaceModule fastPlace = client.getModuleManager().getModule(FastPlaceModule.class);
        if (fastPlace == null || !fastPlace.isEnabled()) {
            return;
        }
        if (fastPlace.isOnlyBlocks()) {
            MinecraftClient self = (MinecraftClient) (Object) this;
            if (self.player == null || !(self.player.getMainHandStack().getItem() instanceof BlockItem)) {
                return;
            }
        }
        itemUseCooldown = fastPlace.getDelay();
    }
}
