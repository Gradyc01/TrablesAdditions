package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Interfaces.EntityInteractable;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractListener extends TrablesListeners {

    @EventHandler
    public void interactingWithEntity(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof LivingEntity)) return;
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
//        LivingEntity entity = (LivingEntity) e.getRightClicked();
//        EntityInteraction entityInteraction = EntityInteraction.getEntityInteraction(entity);
//        if (entityInteraction != null) {
//            entityInteraction.interact(e);
//            return;
//        }
        LivingEntity entity = (LivingEntity) e.getRightClicked();
        EntityInteractable interactable = TrablesAdditions.getInstance().getEntityInteractions().getEntityInteraction(entity);
        if (interactable != null) interactable.interact(PlayerUtil.getPlayerData(e.getPlayer()), e);
    }
}
