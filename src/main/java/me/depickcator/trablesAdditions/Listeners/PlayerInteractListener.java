package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.EntityInteractable;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.UI.Interfaces.BlockUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.server.dialog.input.TextInput;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.inventory.InventoryHolder;

import java.util.Set;

public class PlayerInteractListener extends TrablesListeners {
    private final Set<Material> unRightClickables = Set.of(Material.CRAFTING_TABLE, Material.CARTOGRAPHY_TABLE);
    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        RealmController controller = RealmController.getController(p.getWorld().getName());
        if (controller != null) {
            if (!controller.getRealmState().onPlayerInteract(e, controller)) {
                return;
            }
        }

        PlayerData pD = PlayerUtil.getPlayerData(p);
        boolean blockHasInventory = blockHasInventory(e, pD);
        if (e.getItem() == null) return;
        ItemClick itemClick = ItemClick.findClickItem(e.getItem());
        if (itemClick != null && pD != null && !blockHasInventory) {
            e.setCancelled(!itemClick.uponClick(e, pD));
        }
    }


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

    @EventHandler
    public void onPlayerFillBucket(PlayerBucketFillEvent event) {
        String worldName = event.getBlock().getWorld().getName();
        RealmController controller = RealmController.getController(worldName);
        if (controller != null) controller.getRealmState().onPlayerBucket(event);
    }

    @EventHandler
    public void onPlayerEmptyBucket(PlayerBucketEmptyEvent event) {
        String worldName = event.getBlock().getWorld().getName();
        RealmController controller = RealmController.getController(worldName);
        if (controller != null) controller.getRealmState().onPlayerBucket(event);
    }

    /*Returns True if when the player interacts with a block with a Inventory
     * returns False otherwise*/
    private boolean blockHasInventory(PlayerInteractEvent e, PlayerData pD) {
        if (e.getClickedBlock() == null || e.getClickedBlock().getType() == Material.AIR) return false;
        Block b = e.getClickedBlock();
        BlockUI gui = TrablesBlockGUI.findGUI(b);
//        TextUtil.debugText("Does Block " + b.getType() + " have a Inventory");
        if (gui != null) {
//            if (gui.interactWithBlock(pD, b, e)) e.setCancelled(true);
            e.setCancelled(!gui.interactWithBlock(pD, b, e));
            return true;
        }
        if (b.getBlockData() instanceof Openable) return true;
        if (unRightClickables.contains(b.getType())) return true;
        return b.getState() instanceof InventoryHolder;
    }
}
