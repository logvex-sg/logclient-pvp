package com.logvex.logclient.mixin;

import com.logvex.logclient.LogClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @ModifyVariable(method = "sendMessage", at = @At("HEAD"), argsOnly = true)
    private String logclient$chatSuffix(String message) {
        LogClient client = LogClient.getInstance();
        if (client == null) {
            return message;
        }
        return client.modifyChatMessage(message);
    }
}
