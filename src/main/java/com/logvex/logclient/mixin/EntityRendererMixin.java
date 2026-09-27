package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.optimization.FpsBoostModule;
import com.logvex.logclient.module.visual.EspModule;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Render state is built once per entity per frame, so tinting the outline colour here gives
 * glow ESP without touching the render pipeline.
 */
@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void logclient$updateState(Entity entity, EntityRenderState state, float tickDelta, CallbackInfo ci) {
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return;
        }
        EspModule esp = client.getModuleManager().getModule(EspModule.class);
        if (esp != null && esp.shouldGlow(entity)) {
            state.outlineColor = ColorHelper.fullAlpha(esp.getGlowColor());
        }
        FpsBoostModule boost = client.getModuleManager().getModule(FpsBoostModule.class);
        if (boost != null && boost.isEnabled() && boost.isNoNametags()) {
            state.displayName = Text.empty();
            state.nameLabelPos = null;
        }
    }
}
