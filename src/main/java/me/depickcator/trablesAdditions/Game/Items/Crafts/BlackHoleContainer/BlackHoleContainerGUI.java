package me.depickcator.trablesAdditions.Game.Items.Crafts.BlackHoleContainer;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerInventories;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlackHoleContainerGUI extends TrablesPlayerMenuGUI {
    private final ItemStack sendOutOfRealm;
    private final List<ItemStack> exportedItems;
    private final int exportSize = 28;
    public BlackHoleContainerGUI(PlayerData playerData) {
        super(playerData, 6, TextUtil.makeText("Black Hole Container", TextUtil.BLACK), true);
        sendOutOfRealm = initSendOutOfRealm();
        exportedItems = new ArrayList<>();
        inventory.setItem(49, sendOutOfRealm);
        initItems();
    }

    private void initItems() {
        int index = 0;
        ItemStack air = new ItemStack(Material.AIR);
        for (int i = 9; i < 45; i++) {
            if (i % 9 == 0 || i % 9 == 8) continue;
            ItemStack item = index >= exportedItems.size() ? air : exportedItems.get(index++);
            inventory.setItem(i, item);
        }
    }

    private void exportItems() {
        if (exportedItems.isEmpty()) return;
        player.playSound(SoundUtil.makeSound(Sound.BLOCK_PORTAL_TRAVEL, 0.1F, 2));
        PlayerInventories playerInventories = playerData.getPlayerInventories();
        ItemStack[] items = playerInventories.getCorrespondingInventoryContents(PlayerInventories.REWARDS);
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item == null || item.getType() == Material.AIR) {
                if (exportedItems.isEmpty()) break;
                items[i] = exportedItems.removeFirst();
            }
        }
        playerInventories.setContent(PlayerInventories.REWARDS, items);
        if (exportedItems.isEmpty()) return;
        PlayerUtil.giveItem(player, exportedItems);
        player.sendMessage(TextUtil.makeText("Your Loot Drop Container is full and can no longer carry anymore items", TextUtil.RED));
    }

    private boolean addItem(ItemStack item, Player player, int index) {
        if (exportedItems.size() >= exportSize) return false;
        exportedItems.add(item);
        player.getInventory().setItem(index, new ItemStack(Material.AIR));
        initItems();
        return true;
    }

    private boolean removeItem(ItemStack item, Player player) {
        if (exportedItems.contains(item)) {
            exportedItems.remove(item);
            PlayerUtil.giveItem(player, item);
            initItems();
            return true;
        }
        return false;
    }

    @Override
    public boolean runWhenCloseGUI(PlayerData playerData, InventoryCloseEvent event) {
        exportItems();
        return super.runWhenCloseGUI(playerData, event);
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = playerData.getPlayer();
        if (item == null || ItemComparison.equalItems(item, BlackHoleContainer.getInstance().getResult())) return false;
        if (item.equals(sendOutOfRealm)) {
            event.setCancelled(true);
            player.closeInventory();
            return false;
        }
        if (event.getClickedInventory().equals(inventory)) {
            playSound(player, removeItem(item, player));
        } else {
            playSound(player, addItem(item, player, event.getSlot()));
        }
        return false;
    }

    private void playSound(Player player, boolean success) {
        if (success) {
            SoundUtil.playHighPitchPling(player);
        } else {
            SoundUtil.playErrorSoundEffect(player);
        }
    }

    private ItemStack initSendOutOfRealm() {
        ItemStack item = initExplainerItem(Material.GREEN_WOOL,
                List.of(TextUtil.makeText("Exports everything to the outside world", TextUtil.DARK_PURPLE)),
                TextUtil.makeText("Send Outside", TextUtil.DARK_GREEN));
        return item;
    }
}
