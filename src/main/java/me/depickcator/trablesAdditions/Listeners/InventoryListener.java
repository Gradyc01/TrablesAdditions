package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemDrop;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener extends TrablesListeners {

    /*Detects when a player clicks in an inventory*/
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        PlayerData pD = PlayerUtil.getPlayerData(player);
        Inventory inventory = e.getInventory();
        Pair<Inventory, TrablesGUI> playerGUI = TrablesGUI.findInventory(player);
        if (playerGUI == null) {
            ItemDrop itemDrop = ItemDrop.findDropItem(e.getCursor());
            if (itemDrop != null && e.getCurrentItem() !=null && e.getCurrentItem().getType() != Material.AIR) {
                itemDrop.uponApply(e, e.getCurrentItem(), e.getCursor(), PlayerUtil.getPlayerData(player));
            }
        }
        if (playerGUI != null && inventory == playerGUI.getLeft()) {
            if (playerGUI.getRight() instanceof TrablesMenuGUI && e.getCurrentItem() == null) return;
            if (!playerGUI.getRight().interactWithGUIButtons(pD, e)) {
                e.setCancelled(true);
            }
            if (playerGUI.getRight() instanceof TrablesMenuGUI) e.setCancelled(true);
        }
    }

    /*Detects when a player closes an inventory*/
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Pair<Inventory, TrablesGUI> gui = TrablesGUI.findInventory(player);
        PlayerData playerData = PlayerUtil.getPlayerData(player);
        if (playerData == null) return;
        if (gui == null || gui.getRight().runWhenCloseGUI(PlayerUtil.getPlayerData(player), event)) {
            TrablesGUI.removeGUI(player);
        }
    }
}
