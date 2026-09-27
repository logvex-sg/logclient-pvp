package com.logvex.logclient.module.player;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;

public class InventoryMoveModule extends Module {
    public InventoryMoveModule() {
        super("InventoryMove", "Allows movement while a container is open", Category.PLAYER, 0);
    }
}
