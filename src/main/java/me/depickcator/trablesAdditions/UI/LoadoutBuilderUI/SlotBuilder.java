package me.depickcator.trablesAdditions.UI.LoadoutBuilderUI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerInventories;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SlotBuilder implements TrablesMenuActionable {
    private final String slotName;
    private final int slotIndex;
    private final int realmInvIndex;
    private final boolean isArmorSlot;
    private final ItemStack emptySlot;
    private final boolean isHotbar;
    private final Inventory inventory;
    private final PlayerInventories playerInventories;
    private final EquipmentSlot equipmentSlot;
    private boolean hasItem;
    private ItemStack currentItem;
    public SlotBuilder(String slotName, int slotIndex, int realmInvIndex, boolean isArmorSlot, TrablesPlayerMenuGUI gui, EquipmentSlot equipmentSlot) {
        this.slotName = slotName;
        this.slotIndex = slotIndex;
        this.realmInvIndex = realmInvIndex;
        this.isArmorSlot = isArmorSlot;
        this.isHotbar = realmInvIndex < 9 || realmInvIndex == 40;
        this.inventory = gui.getInventory();
        this.playerInventories = gui.getPlayerData().getPlayerInventories();
        this.equipmentSlot = equipmentSlot;
        this.emptySlot = initEmptySlot();
        ItemStack item = this.playerInventories.getCorrespondingInventoryContent(PlayerInventories.REALM_INV_KEY, realmInvIndex);
        if (item != null && item.getType() != Material.AIR) setItem(item);
        else setEmpty();
    }

    public SlotBuilder(String slotName, int slotIndex, int realmInvIndex, TrablesPlayerMenuGUI gui) {
        this(slotName, slotIndex, realmInvIndex, false, gui, null);
    }

    private ItemStack initEmptySlot() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.customName(TextUtil.makeText(slotName, TextUtil.RED));
        item.setItemMeta(meta);
        return item;
    }

    private void setEmpty() {
        hasItem = false;
        currentItem = emptySlot;
        inventory.setItem(slotIndex, emptySlot);
    }

    private void setItem(ItemStack item) {
        hasItem = true;
        currentItem = item;
        inventory.setItem(slotIndex, item);

    }

    public void solidify() {
        ItemStack[] arr = this.playerInventories.getCorrespondingInventoryContents(PlayerInventories.REALM_INV_KEY);
        arr[realmInvIndex] = hasItem ? this.currentItem : new ItemStack(Material.AIR);
        this.playerInventories.setContent(PlayerInventories.REALM_INV_KEY, arr);
    }

    @Override
    public boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event) {
        ItemStack clickedOn = event.getCurrentItem();
        Player player = playerData.getPlayer();
        ItemStack clickedWith = event.getCursor();
        if (clickedOn == null) return false;
        if (!hasCursorItem(event) && hasItem) {
            player.setItemOnCursor(currentItem);
            setEmpty();
        } else if (hasCursorItem(event) && !hasItem) {
            setItem(clickedWith);
            player.setItemOnCursor(new ItemStack(Material.AIR));
        } else if (hasCursorItem(event) && hasItem) {
            ItemStack cursorItem = clickedWith.clone();
            player.setItemOnCursor(currentItem);
            setItem(cursorItem);
        } else if (!hasCursorItem(event) && !hasItem && (isArmorSlot || isHotbar)) {
//            ItemStack equipmentItem = player.getEquipment().getItem(equipmentSlot);
//            if (equipmentItem.getType() != Material.AIR) {
//                player.getEquipment().setItem(equipmentSlot, new ItemStack(Material.AIR));
//                setItem(equipmentItem);
//            }
            ItemStack item = player.getInventory().getItem(realmInvIndex);
            if (item != null && item.getType() != Material.AIR) {
                player.getInventory().setItem(realmInvIndex, new ItemStack(Material.AIR));
                setItem(item);
            }
        }
        return true;
    }

    public void removeItem(Player player) {
        if (hasItem) {
            PlayerUtil.giveItem(player, currentItem);
            setEmpty();
        }
    }

    private boolean hasCursorItem(InventoryClickEvent event) {
        return event.getCursor().getType() != Material.AIR;
    }
}
