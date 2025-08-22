package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.Set;

public class MobSpawning extends TrablesListeners {
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        String worldName = event.getLocation().getWorld().getName();
        if (Set.of("world", "world_nether", "world_the_end").contains(worldName.toLowerCase())) return;
        RealmController controller = RealmController.getController(worldName);
        controller.getRealm().onMobSpawn(event);
    }
}
