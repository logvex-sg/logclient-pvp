package com.logvex.logclient.module.misc;

import com.logvex.logclient.LogClient;
import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.BooleanSetting;
import com.logvex.logclient.setting.StringSetting;

public class ChatSuffixModule extends Module {
    private final StringSetting suffix = text("Suffix", "Text appended to your messages", " | LogClient");
    private final BooleanSetting onlyNormal = bool("OnlyNormal", "Do not append to commands", true);

    public ChatSuffixModule() {
        super("ChatSuffix", "Appends a signature to your chat messages", Category.MISC, 0);
    }

    public String modify(String message) {
        if (!isEnabled() || message.isEmpty()) {
            return message;
        }
        if (onlyNormal.get() && (message.startsWith("/") || message.startsWith("!"))) {
            return message;
        }
        return message + suffix.get();
    }
}
