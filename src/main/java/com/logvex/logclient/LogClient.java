package com.logvex.logclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.logvex.logclient.config.ConfigManager;
import com.logvex.logclient.gui.GuiManager;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.module.ModuleManager;
import com.logvex.logclient.module.combat.AutoClickerModule;
import com.logvex.logclient.module.combat.AutoTotemModule;
import com.logvex.logclient.module.combat.CriticalsModule;
import com.logvex.logclient.module.combat.KillAuraModule;
import com.logvex.logclient.module.combat.ReachModule;
import com.logvex.logclient.module.combat.VelocityModule;
import com.logvex.logclient.module.combat.WTapModule;
import com.logvex.logclient.module.hud.ArmorModule;
import com.logvex.logclient.module.hud.ArrayListModule;
import com.logvex.logclient.module.hud.CoordinatesModule;
import com.logvex.logclient.module.hud.FpsModule;
import com.logvex.logclient.module.hud.HudModule;
import com.logvex.logclient.module.hud.PingModule;
import com.logvex.logclient.module.hud.PotionModule;
import com.logvex.logclient.module.hud.ScoreboardHudModule;
import com.logvex.logclient.module.hud.SpeedModule;
import com.logvex.logclient.module.hud.TargetHudModule;
import com.logvex.logclient.module.hud.WatermarkModule;
import com.logvex.logclient.module.misc.AutoReconnectModule;
import com.logvex.logclient.module.misc.ChatSuffixModule;
import com.logvex.logclient.module.misc.CopyCoordsModule;
import com.logvex.logclient.module.misc.MiddleClickModule;
import com.logvex.logclient.module.optimization.EntityCullingModule;
import com.logvex.logclient.module.optimization.FpsBoostModule;
import com.logvex.logclient.module.optimization.NoFogModule;
import com.logvex.logclient.module.optimization.NoWeatherModule;
import com.logvex.logclient.module.optimization.ParticleLimitModule;
import com.logvex.logclient.module.player.AutoRespawnModule;
import com.logvex.logclient.module.player.AutoToolModule;
import com.logvex.logclient.module.player.FastPlaceModule;
import com.logvex.logclient.module.player.InventoryMoveModule;
import com.logvex.logclient.module.player.NoFallModule;
import com.logvex.logclient.module.player.SafeWalkModule;
import com.logvex.logclient.module.player.SprintModule;
import com.logvex.logclient.module.visual.EspModule;
import com.logvex.logclient.module.visual.FullbrightModule;
import com.logvex.logclient.module.visual.NameTagsModule;
import com.logvex.logclient.module.visual.NoHurtCamModule;
import com.logvex.logclient.module.visual.NoRenderModule;
import com.logvex.logclient.module.visual.ViewClipModule;
import com.logvex.logclient.module.visual.ZoomModule;
import com.logvex.logclient.util.KeybindManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class LogClient implements ClientModInitializer {
    public static final String NAME = "LogClient";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static LogClient instance;

    private final ModuleManager moduleManager = new ModuleManager();
    private final GuiManager guiManager = new GuiManager();

    public static LogClient getInstance() {
        return instance;
    }

    public static MinecraftClient mc() {
        return MinecraftClient.getInstance();
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        registerModules();
        KeybindManager.init();
        ConfigManager.load();
        registerEvents();
        LOGGER.info("{} v{} loaded {} modules", NAME, VERSION, moduleManager.getModules().size());
    }

    private void registerModules() {
        moduleManager.register(new KillAuraModule());
        moduleManager.register(new AutoClickerModule());
        moduleManager.register(new ReachModule());
        moduleManager.register(new CriticalsModule());
        moduleManager.register(new VelocityModule());
        moduleManager.register(new AutoTotemModule());
        moduleManager.register(new WTapModule());

        moduleManager.register(new EspModule());
        moduleManager.register(new NameTagsModule());
        moduleManager.register(new FullbrightModule());
        moduleManager.register(new ZoomModule());
        moduleManager.register(new NoRenderModule());
        moduleManager.register(new ViewClipModule());
        moduleManager.register(new NoHurtCamModule());

        moduleManager.register(new SprintModule());
        moduleManager.register(new AutoToolModule());
        moduleManager.register(new FastPlaceModule());
        moduleManager.register(new SafeWalkModule());
        moduleManager.register(new NoFallModule());
        moduleManager.register(new InventoryMoveModule());
        moduleManager.register(new AutoRespawnModule());

        moduleManager.register(new FpsBoostModule());
        moduleManager.register(new EntityCullingModule());
        moduleManager.register(new ParticleLimitModule());
        moduleManager.register(new NoFogModule());
        moduleManager.register(new NoWeatherModule());

        moduleManager.register(new AutoReconnectModule());
        moduleManager.register(new CopyCoordsModule());
        moduleManager.register(new MiddleClickModule());
        moduleManager.register(new ChatSuffixModule());

        moduleManager.register(new FpsModule());
        moduleManager.register(new CoordinatesModule());
        moduleManager.register(new PingModule());
        moduleManager.register(new SpeedModule());
        moduleManager.register(new ArrayListModule());
        moduleManager.register(new WatermarkModule());
        moduleManager.register(new ArmorModule());
        moduleManager.register(new PotionModule());
        moduleManager.register(new TargetHudModule());
        moduleManager.register(new ScoreboardHudModule());
    }

    private void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            KeybindManager.handleInput();
            moduleManager.tickClient();
            if (client.player != null && client.world != null) {
                moduleManager.tick();
            }
        });

        HudRenderCallback.EVENT.register(this::renderHud);

        Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::save, "logclient-config-save"));
    }

    private void renderHud(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null || client.options.hudHidden) {
            return;
        }
        float tickDelta = tickCounter.getDynamicDeltaTicks();
        for (Module module : moduleManager.getEnabled()) {
            if (module instanceof HudModule hud) {
                hud.renderScaled(context, client, tickDelta);
            }
        }
        EspModule esp = moduleManager.getModule(EspModule.class);
        if (esp != null) {
            esp.renderWorld(context, client, tickDelta);
        }
        NameTagsModule nameTags = moduleManager.getModule(NameTagsModule.class);
        if (nameTags != null) {
            nameTags.renderWorld(context, client, tickDelta);
        }
    }

    public void onKeyPressed(int keyCode) {
        moduleManager.onKeyPressed(keyCode);
    }

    public void onMousePressed(int button) {
        if (button == 2) {
            MiddleClickModule middleClick = moduleManager.getModule(MiddleClickModule.class);
            if (middleClick != null) {
                middleClick.onMiddleClick(MinecraftClient.getInstance());
            }
        }
    }

    public String modifyChatMessage(String message) {
        ChatSuffixModule suffix = moduleManager.getModule(ChatSuffixModule.class);
        return suffix != null ? suffix.modify(message) : message;
    }

    public boolean isSafeWalk() {
        SafeWalkModule module = moduleManager.getModule(SafeWalkModule.class);
        return module != null && module.shouldHold(MinecraftClient.getInstance());
    }

    public boolean isNoFall() {
        NoFallModule module = moduleManager.getModule(NoFallModule.class);
        return module != null && module.shouldSendPacket(MinecraftClient.getInstance());
    }

    public FastPlaceModule fastPlace() {
        return moduleManager.getModule(FastPlaceModule.class);
    }

    public boolean isFpsBoost(String option) {
        FpsBoostModule module = moduleManager.getModule(FpsBoostModule.class);
        return module != null && module.isEnabled() && switch (option) {
            case "Particles" -> module.isNoParticles();
            case "Clouds" -> module.isNoClouds();
            case "BlockBreak" -> module.isNoBlockBreak();
            case "Nametags" -> module.isNoNametags();
            default -> false;
        };
    }

    public boolean isZooming() {
        ZoomModule module = moduleManager.getModule(ZoomModule.class);
        return module != null && module.isEnabled() && module.isZooming();
    }

    public boolean shouldGlow(Entity entity) {
        EspModule esp = moduleManager.getModule(EspModule.class);
        return esp != null && esp.shouldGlow(entity);
    }

    public boolean shouldCull(Entity entity) {
        EntityCullingModule culling = moduleManager.getModule(EntityCullingModule.class);
        return culling != null && culling.shouldCull(entity);
    }

    public boolean isInventoryMove() {
        InventoryMoveModule module = moduleManager.getModule(InventoryMoveModule.class);
        return module != null && module.isEnabled();
    }

    public boolean isNoRender(String option) {
        NoRenderModule module = moduleManager.getModule(NoRenderModule.class);
        return module != null && module.isEnabled() && module.isNoRenderEnabled(option);
    }

    public boolean isViewClip() {
        ViewClipModule module = moduleManager.getModule(ViewClipModule.class);
        return module != null && module.isEnabled();
    }

    public boolean isNoHurtCam() {
        NoHurtCamModule module = moduleManager.getModule(NoHurtCamModule.class);
        return module != null && module.isEnabled();
    }

    public boolean isNoFog() {
        NoFogModule module = moduleManager.getModule(NoFogModule.class);
        return module != null && module.isEnabled();
    }

    /** Returns the fog module only while it is enabled, so mixins can guard on a single null check. */
    public static NoFogModule noFog() {
        NoFogModule module = instance == null ? null : instance.moduleManager.getModule(NoFogModule.class);
        return module != null && module.isEnabled() ? module : null;
    }

    public boolean isNoWeather() {
        NoWeatherModule module = moduleManager.getModule(NoWeatherModule.class);
        return module != null && module.isEnabled();
    }

    public int particleLimit() {
        ParticleLimitModule module = moduleManager.getModule(ParticleLimitModule.class);
        return module != null && module.isEnabled() ? module.getLimit() : -1;
    }

    public float zoomDivisor() {
        ZoomModule module = moduleManager.getModule(ZoomModule.class);
        return module != null && module.isEnabled() ? module.getDivisor() : 1f;
    }
}
