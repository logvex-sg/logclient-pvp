package com.logvex.logclient.module.combat;

import com.logvex.logclient.module.Category;
import com.logvex.logclient.module.Module;
import com.logvex.logclient.setting.IntSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ReachModule extends Module {
    private static final Identifier ENTITY_ID = Identifier.of("logclient", "reach_entity");
    private static final Identifier BLOCK_ID = Identifier.of("logclient", "reach_block");

    private final IntSetting entityReach = integer("EntityReach", "Extra entity reach in blocks", 1, 0, 5);
    private final IntSetting blockReach = integer("BlockReach", "Extra block reach in blocks", 1, 0, 5);

    public ReachModule() {
        super("Reach", "Extends your interaction range", Category.COMBAT, 0);
    }

    @Override
    public void onEnable() {
        apply();
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) {
            return;
        }
        remove(mc.player, EntityAttributes.ENTITY_INTERACTION_RANGE, ENTITY_ID);
        remove(mc.player, EntityAttributes.BLOCK_INTERACTION_RANGE, BLOCK_ID);
    }

    @Override
    public void onTick() {
        apply();
    }

    private void apply() {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;
        if (player == null) {
            return;
        }
        set(player, EntityAttributes.ENTITY_INTERACTION_RANGE, ENTITY_ID, entityReach.get());
        set(player, EntityAttributes.BLOCK_INTERACTION_RANGE, BLOCK_ID, blockReach.get());
    }

    private void set(Entity entity, net.minecraft.registry.entry.RegistryEntry<net.minecraft.entity.attribute.EntityAttribute> attribute,
                     Identifier id, double value) {
        EntityAttributeInstance instance = ((net.minecraft.entity.LivingEntity) entity).getAttributeInstance(attribute);
        if (instance == null) {
            return;
        }
        instance.removeModifier(id);
        if (value > 0) {
            instance.addPersistentModifier(new EntityAttributeModifier(id, value, EntityAttributeModifier.Operation.ADD_VALUE));
        }
    }

    private void remove(Entity entity, net.minecraft.registry.entry.RegistryEntry<net.minecraft.entity.attribute.EntityAttribute> attribute,
                        Identifier id) {
        EntityAttributeInstance instance = ((net.minecraft.entity.LivingEntity) entity).getAttributeInstance(attribute);
        if (instance != null) {
            instance.removeModifier(id);
        }
    }
}
