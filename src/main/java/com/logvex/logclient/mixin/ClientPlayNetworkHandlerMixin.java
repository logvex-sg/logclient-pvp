package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.combat.VelocityModule;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Scaling the velocity before it reaches the entity keeps the reduction client-side only.
 */
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Redirect(method = "onEntityVelocityUpdate",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;setVelocityClient(Lnet/minecraft/util/math/Vec3d;)V"))
    private void logclient$velocity(Entity entity, Vec3d velocity) {
        LogClient client = LogClient.getInstance();
        VelocityModule module = client == null ? null : client.getModuleManager().getModule(VelocityModule.class);
        if (module != null && module.isEnabled() && client.mc().player != null
                && entity.getId() == client.mc().player.getId()) {
            entity.setVelocityClient(module.modify(client.mc(), velocity));
            return;
        }
        entity.setVelocityClient(velocity);
    }
}
