package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
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
            String worldString = b.getMetadata(DIMENSIONAL_TRAVEL_KEY).getFirst().asString();
            TextUtil.debugText(player.getName() + " has dimensionality travelling" + worldString);
            RealmController controller = RealmController.getController(worldString);
            if (controller != null) {
                event.setCanCreatePortal(false);
//                Location teleportLocation = controller.getSpawnLocation();
//                if (teleportLocation == null) event.setCancelled(true);
//                else {
//                    event.setTo(teleportLocation);
//                    player.teleport(teleportLocation);
//                }
                controller.joinWorld(PlayerUtil.getPlayerData(player));
                event.setCancelled(true);

            }
        }
        TextUtil.debugText("Dimensional Travel", debugText);
    }

    /*Checks the surrounding area for a block that has a metadata tag of DIMENSIONAL_TRAVEL_KEY and returns it.
    * Returns null otherwise*/
    private Block findPortalFrames(Location loc) {
        for (int x = -2; x <= 2; x++ ) {
            for (int y = -2; y <= 2; y++ ) {
                for (int z = -2; z <= 2; z++ ) {
                    Location l = loc.clone().add(x, y, z);
                    Block block = l.getBlock();
//                    TextUtil.debugText("Checking Block " + block.getType().name());
                    if (l.getBlock().hasMetadata(DIMENSIONAL_TRAVEL_KEY) && l.getBlock().getType().equals(Material.NETHER_PORTAL)) {
                        TextUtil.debugText("Found Block " + block.getType().name());
                        return l.getBlock();
                    }
                }
            }
        }
        return null;
    }
}
