package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.TrablesAdditions;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Set;

public class EntityDeath extends TrablesListeners {
    private final Set<String> WHITELISTED_WORLDS = Set.of("world", "world_nether", "world_the_end");
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        String worldName = event.getEntity().getWorld().getName();
        if (WHITELISTED_WORLDS.contains(worldName.toLowerCase())) return;
        RealmController controller = RealmController.getController(worldName);
        controller.getRealm().onEntityDeath(event);
    }
}
