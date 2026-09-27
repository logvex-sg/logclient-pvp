package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.optimization.FpsBoostModule;
import com.logvex.logclient.module.optimization.ParticleLimitModule;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Queue;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Shadow
    private Queue<Particle> newParticles;

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At("HEAD"), cancellable = true)
    private void logclient$limit(ParticleEffect effect, double x, double y, double z, double velocityX,
                                 double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return;
        }
        FpsBoostModule boost = client.getModuleManager().getModule(FpsBoostModule.class);
        if (boost != null && boost.isEnabled() && boost.isNoParticles()) {
            cir.setReturnValue(null);
            return;
        }
        ParticleLimitModule limit = client.getModuleManager().getModule(ParticleLimitModule.class);
        if (limit != null && limit.isEnabled() && newParticles != null && newParticles.size() >= limit.getLimit()) {
            cir.setReturnValue(null);
        }
    }
}
