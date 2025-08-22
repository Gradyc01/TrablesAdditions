package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;

public class DimensionalTravel extends TrablesListeners {
    public static String DIMENSIONAL_TRAVEL_KEY = "Traverse";
    @EventHandler
    public void onPlayerInteract(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        String debugText = "Player " + player.getName() + "was originally going from "
                + event.getFrom().getWorld().getName() + " to " + event.getTo().getWorld().getName() + " ";
        Block b = findPortalFrames(event.getFrom());
        if (b!=null) {
            TextUtil.debugText(player.getName() + " has dimensionality travelling");
            String worldString = b.getMetadata(DIMENSIONAL_TRAVEL_KEY).getFirst().asString();
            RealmController controller = RealmController.getController(worldString);
            if (controller != null) {
                event.setCanCreatePortal(false);
                Location teleportLocation = controller.getSpawnLocation();
                if (teleportLocation == null) event.setCancelled(true);
                else {
                    event.setTo(teleportLocation);
                    player.teleport(teleportLocation);
                }

            }
        }
        TextUtil.debugText("Dimensional Travel", debugText);
    }

    /*Checks the surrounding area for a block that has a metadata tag of DIMENSIONAL_TRAVEL_KEY and returns it.
    * Returns null otherwise*/
    private Block findPortalFrames(Location loc) {
        for (int x = -1; x <= 1; x++ ) {
            for (int y = -1; y <= 1; y++ ) {
                for (int z = -1; z <= 1; z++ ) {
                    Location l = loc.clone().add(x, y, z);
                    if (l.getBlock().hasMetadata(DIMENSIONAL_TRAVEL_KEY)) {
                        return l.getBlock();
                    }
                }
            }
        }
        return null;
    }
}
