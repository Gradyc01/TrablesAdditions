package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface ItemClick {
    Map<String, ItemClick> items = new HashMap<>();
//    /* Gets the item in the ItemClick*/
//    ItemStack getItem();
    /* What happens when it is clicked
    Returns True if successful
    False Otherwise*/
    boolean uponClick(PlayerInteractEvent e, PlayerData pD);

    default void registerItem(CustomItem customItem, ItemClick itemClick) {
        items.put(ItemComparison.itemParser(customItem.getResult()), itemClick);
    }

    static ItemClick findClickItem(ItemStack item) {
        if (item == null) return null;
        return items.get(ItemComparison.itemParser(item));
    }

}
