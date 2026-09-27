package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Rewrites the ground flag only while the outgoing movement packet is built, so the server
 * never registers a fall. The player's real ground state is left untouched for physics.
 */
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @WrapOperation(method = "sendMovementPackets",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isOnGround()Z"))
    private boolean logclient$noFall(ClientPlayerEntity self, Operation<Boolean> original) {
        LogClient client = LogClient.getInstance();
        if (client != null && client.isNoFall()) {
            return true;
        }
        return original.call(self);
    }
}
