package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.player.InventoryMoveModule;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla clears the movement key states whenever a screen opens, which is why walking while
 * a container is up stops working. Rebuilding the input from the raw key state keeps the
 * player moving.
 */
@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {
    @Shadow
    @Final
    private GameOptions settings;

    @Inject(method = "tick", at = @At("TAIL"))
    private void logclient$inventoryMove(CallbackInfo ci) {
        LogClient client = LogClient.getInstance();
        if (client == null || !client.isInventoryMove()) {
            return;
        }
        if (client.mc().currentScreen == null) {
            return;
        }
        PlayerInput input = new PlayerInput(
                down(settings.forwardKey),
                down(settings.backKey),
                down(settings.leftKey),
                down(settings.rightKey),
                down(settings.jumpKey),
                down(settings.sneakKey),
                down(settings.sprintKey));
        this.playerInput = input;
        float forward = (input.forward() ? 1f : 0f) - (input.backward() ? 1f : 0f);
        float sideways = (input.left() ? 1f : 0f) - (input.right() ? 1f : 0f);
        this.movementVector = new Vec2f(sideways, forward).normalize();
    }

    private static boolean down(net.minecraft.client.option.KeyBinding binding) {
        InputUtil.Key key = InputUtil.fromTranslationKey(binding.getBoundKeyTranslationKey());
        if (key.getCategory() != InputUtil.Type.KEYSYM) {
            return binding.isPressed();
        }
        return GLFW.glfwGetKey(LogClient.mc().getWindow().getHandle(), key.getCode()) == GLFW.GLFW_PRESS;
    }
}
