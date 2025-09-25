package me.depickcator.trablesAdditions.UI.LoadoutBuilderUI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.UI.MainMenuGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoadoutBuilderGUI extends TrablesPlayerMenuGUI {
    private final Map<Integer, SlotBuilder> actions;
    private final ItemStack removeAllItem;
    public LoadoutBuilderGUI(PlayerData playerData) {
        super(playerData, 6, TextUtil.makeText("Build Your Loadout", TextUtil.GOLD), true);
        actions = new HashMap<>();
        initSlots();
        inventory.setItem(48, goBackItem());
        playerHeadButton(49);
        removeAllItem = initRemoveAllItem();
        inventory.setItem(53, removeAllItem);
    }

    private void initSlots() {
        initSlot("Hotbar 1", 36, 0);
        initSlot("Hotbar 2", 37, 1);
        initSlot("Hotbar 3", 38, 2);
        initSlot("Hotbar 4", 39, 3);
        initSlot("Hotbar 5", 40, 4);
        initSlot("Hotbar 6", 41, 5);
        initSlot("Hotbar 7", 42, 6);
        initSlot("Hotbar 8", 43, 7);
        initSlot("Hotbar 9", 44, 8);
        initSlot("Head", 0, 39, EquipmentSlot.HEAD);
        initSlot("Chest", 1, 38, EquipmentSlot.CHEST);
        initSlot("Legs", 2, 37, EquipmentSlot.LEGS);
        initSlot("Boots", 3, 36, EquipmentSlot.FEET);
        initSlot("Offhand", 8, 40);

        initSlot("Reserve Slot 1", 27, 9);
        initSlot("Reserve Slot 2", 28, 10);
        initSlot("Reserve Slot 3", 29, 11);
        initSlot("Reserve Slot 4", 30, 12);
    }

    private void initSlot(String slotName, int index, int realmIndex) {
        actions.put(index, new SlotBuilder(slotName, index, realmIndex, this));
    }
    private void initSlot(String slotName, int index, int realmIndex, EquipmentSlot equipmentSlot) {
        actions.put(index, new SlotBuilder(slotName, index, realmIndex, true, this, equipmentSlot));
    }
    private ItemStack initRemoveAllItem() {
        return initExplainerItem(Material.RED_WOOL, List.of(), TextUtil.makeText("Reset Loadout", TextUtil.RED));
    }

    @Override
    public boolean runWhenCloseGUI(PlayerData playerData, InventoryCloseEvent event) {
        for (SlotBuilder slotBuilder : actions.values()) {
            slotBuilder.solidify();
        }
        return super.runWhenCloseGUI(playerData, event);
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null || Set.of("shulker_box", "bundle").contains(item.getType().name().toLowerCase())) return false;
        if (item.equals(goBackItem())) {
            new MainMenuGUI(playerData);
            return false;
        } else if (item.equals(removeAllItem)) {
            for (SlotBuilder slotBuilder : actions.values()) {
                slotBuilder.removeItem(player);
            }
        }
        if (event.getClickedInventory() != player.getInventory()) {
            TrablesMenuActionable action = actions.get(event.getSlot());
            if (action != null) action.runAction(playerData, this, event);
            return false;
        } else {
            return true;
        }
    }
}
