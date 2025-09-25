package me.depickcator.trablesAdditions.UI.DisplayReward;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerInventories;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.UI.MainMenuGUI;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DisplayRewardGUI extends TrablesPlayerMenuGUI implements TrablesMenuActionable {
    private final List<ItemStack> items;
    private final List<ItemStack> displayedItems;
    private final ItemStack claimItems;
    private final ItemStack deleteItems;
    public DisplayRewardGUI(PlayerData playerData) {
        this(playerData, playerData.getPlayerInventories().getCorrespondingInventoryContents(PlayerInventories.REWARDS));
    }

    public DisplayRewardGUI(PlayerData playerData, ItemStack[] items) {
        super(playerData, 6, TextUtil.makeText("View Realm Loot: ", TextUtil.AQUA), true, false);
        this.items = new ArrayList<>();
        for (ItemStack item : items) {
            if (item != null && item.getType() != Material.AIR) this.items.add(item);
        }
        displayedItems = new ArrayList<>();
        initSlots();
        claimItems = initClaimItems();
        inventory.setItem(48, goBackItem());
        deleteItems = initDeleteItems();
        playerHeadButton(49);
    }

    private ItemStack initClaimItems() {
        ItemStack item = initExplainerItem(Material.GREEN_WOOL, List.of(), TextUtil.makeText("Claim All Items", TextUtil.DARK_GREEN));
        inventory.setItem(52, item);
        return item;
    }

    private ItemStack initDeleteItems() {
        ItemStack item = initExplainerItem(Material.RED_WOOL, List.of(), TextUtil.makeText("Delete All Items", TextUtil.DARK_RED));
        inventory.setItem(53, item);
        return item;
    }

    private void initSlots() {
        displayedItems.clear();
        for (int i = 0; i < 45; i++) {
            if (i < items.size()) {
                inventory.setItem(i, items.get(i));
                displayedItems.add(items.get(i));
            } else {
                inventory.setItem(i, new ItemStack(Material.AIR));
            }
        }
        inventory.setItem(45, initExplainerItem(Material.REDSTONE_TORCH, List.of(),
                TextUtil.makeText(items.size() + "/" + PlayerInventories.REWARDS_SIZE + " Storage Limit", TextUtil.DARK_RED)));
    }

    @Override
    public boolean runWhenCloseGUI(PlayerData playerData, InventoryCloseEvent event) {
        ItemStack[] itemStacks = new ItemStack[PlayerInventories.REWARDS_SIZE];
        for (int i = 0; i < items.size(); i++) {
            itemStacks[i] = items.get(i);
        }
        playerData.getPlayerInventories().setContent(PlayerInventories.REWARDS, itemStacks);
        return super.runWhenCloseGUI(playerData, event);
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
//        return false;
        ItemStack item = event.getCurrentItem();
        if (item == null) return false;
        if (item.equals(claimItems)) {
            for (ItemStack itemStack : displayedItems) {
                PlayerUtil.giveItem(player, itemStack);
                items.remove(itemStack);
            }
        } else if (item.equals(deleteItems)) {
            TextUtil.debugText(displayedItems.size() + "");
            for (ItemStack itemStack : displayedItems) {
                items.remove(itemStack);
            }
        } else if (displayedItems.contains(item)) {
            if (!event.isRightClick()) {
                PlayerUtil.giveItem(player, item);
            }
            items.remove(item);
        } else if (item.equals(goBackItem())) {
            new MainMenuGUI(playerData);
            return false;
        } else {
            return false;
        }
        SoundUtil.playHighPitchPling(player);
        initSlots();
        return false;
    }

    @Override
    public boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event) {
        open(player);
        return true;
    }
}
