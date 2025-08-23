package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath extends TrablesListeners {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        String worldName = event.getEntity().getWorld().getName();
        RealmController controller = RealmController.getController(worldName);
        if (controller != null) controller.getRealmState().onEntityDeath(event);
    }
}
