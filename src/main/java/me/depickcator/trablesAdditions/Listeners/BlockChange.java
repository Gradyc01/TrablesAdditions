package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Set;

public class BlockChange extends TrablesListeners {
    private final Set<String> WHITELISTED_WORLDS = Set.of("world", "world_nether", "world_the_end");
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String worldName = event.getBlock().getWorld().getName();
        if (WHITELISTED_WORLDS.contains(worldName.toLowerCase())) return;
        RealmController controller = RealmController.getController(worldName);
        controller.getRealm().onBlockBreak(event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String worldName = event.getBlock().getWorld().getName();
        if (WHITELISTED_WORLDS.contains(worldName.toLowerCase())) return;
        RealmController controller = RealmController.getController(worldName);
        controller.getRealm().onBlockPlace(event);
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent event) {
        String worldName = event.getLocation().getWorld().getName();
        if (WHITELISTED_WORLDS.contains(worldName.toLowerCase())) return;
        RealmController controller = RealmController.getController(worldName);
        controller.getRealm().onEntityExplode(event);
    }
}
