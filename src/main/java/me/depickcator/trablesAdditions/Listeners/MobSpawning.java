package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobSpawning extends TrablesListeners {
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        String worldName = event.getLocation().getWorld().getName();
        RealmController controller = RealmController.getController(worldName);
        if (controller != null) controller.getRealmState().onMobSpawn(event);
    }
}
