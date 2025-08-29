package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EntityDeath extends TrablesListeners {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        String worldName = event.getEntity().getWorld().getName();
        RealmController controller = RealmController.getController(worldName);
        if (controller != null) controller.getRealmState().onEntityDeath(event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        RealmController controller = RealmController.getController(event.getEntity().getWorld().getName());
        if (controller != null) controller.getRealmState().onPlayerDeath(event, controller);
    }
}
