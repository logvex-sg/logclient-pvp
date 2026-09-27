package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.visual.NoRenderModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @WrapOperation(method = "renderOverlays",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;renderInWallOverlay(Lnet/minecraft/client/texture/Sprite;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"))
    private void logclient$noPumpkin(Sprite sprite, MatrixStack matrices, VertexConsumerProvider consumers,
                                     Operation<Void> original) {
        LogClient client = LogClient.getInstance();
        if (client == null || !client.isNoRender(NoRenderModule.PUMPKIN)) {
            original.call(sprite, matrices, consumers);
        }
    }

    @WrapOperation(method = "renderOverlays",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;renderUnderwaterOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"))
    private void logclient$noWater(MinecraftClient client, MatrixStack matrices, VertexConsumerProvider consumers,
                                   Operation<Void> original) {
        if (!LogClient.getInstance().isNoRender(NoRenderModule.WATER)) {
            original.call(client, matrices, consumers);
        }
    }

    @WrapOperation(method = "renderOverlays",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;renderFireOverlay(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/texture/Sprite;)V"))
    private void logclient$noFire(MatrixStack matrices, VertexConsumerProvider consumers, Sprite sprite,
                                  Operation<Void> original) {
        if (!LogClient.getInstance().isNoRender(NoRenderModule.FIRE)) {
            original.call(matrices, consumers, sprite);
        }
    }
}
