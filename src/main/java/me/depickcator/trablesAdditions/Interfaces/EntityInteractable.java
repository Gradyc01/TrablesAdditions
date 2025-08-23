package me.depickcator.trablesAdditions.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface EntityInteractable {

//    /* Triggers when a user interacts with an entity has an EntityInteraction attached */
//    public abstract boolean interact(PlayerInteractEntityEvent event);
    boolean interact(PlayerData playerData, PlayerInteractEntityEvent event);

    LivingEntity getEntity();
    void removeEntity();
}
