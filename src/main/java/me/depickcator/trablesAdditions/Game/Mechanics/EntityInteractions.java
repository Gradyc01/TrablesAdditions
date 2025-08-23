package me.depickcator.trablesAdditions.Game.Mechanics;

import me.depickcator.trablesAdditions.Interfaces.EntityInteractable;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.UUID;

public class EntityInteractions {
    private final HashMap<UUID, EntityInteractable> entityInteractions;
    public EntityInteractions() {
         entityInteractions = new HashMap<>();
    }

    public LivingEntity addInteraction(EntityInteractable interactable) {
        UUID uuid = interactable.getEntity().getUniqueId();
        entityInteractions.put(uuid, interactable);
        return interactable.getEntity();
    }

    public LivingEntity removeInteraction(LivingEntity entity) {
        entityInteractions.remove(entity.getUniqueId());
        return entity;
    }

    public EntityInteractable getEntityInteraction(LivingEntity entity) {
        return entityInteractions.get(entity.getUniqueId());
    }

    public void clearInteractions() {
        entityInteractions.clear();
    }
}
