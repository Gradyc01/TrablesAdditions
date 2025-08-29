package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface ItemDrop {
    Map<String, ItemDrop> items = new HashMap<>();
    /* What happens when ItemStack applying is applied on to ItemStack appliedOn
    Returns True if successful
    False Otherwise*/
    boolean uponApply(InventoryClickEvent e, ItemStack appliedOn, ItemStack applying, PlayerData pD);

    default void registerItem(CustomItem customItem, ItemDrop itemDrop) {
        items.put(ItemComparison.itemParser(customItem.getResult()), itemDrop);
    }

    static ItemDrop findDropItem(ItemStack item) {
        if (item == null) return null;
        return items.get(ItemComparison.itemParser(item));
    }
}
