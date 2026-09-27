package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.optimization.EntityCullingModule;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderManager.class)
public class EntityRenderManagerMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void logclient$cull(E entity, Frustum frustum, double x, double y, double z,
                                                   CallbackInfoReturnable<Boolean> cir) {
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return;
        }
        EntityCullingModule culling = client.getModuleManager().getModule(EntityCullingModule.class);
        if (culling != null && culling.shouldCull(entity)) {
            cir.setReturnValue(false);
        }
    }
}
