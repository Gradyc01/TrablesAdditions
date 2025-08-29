package me.depickcator.trablesAdditions.Util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class ItemComparison {
    /* Returns True if they are equal items, False Otherwise */
    public static boolean equalItems(ItemStack inv, ItemStack board) {
        String invItemStr = itemParser(inv);
        String boardItemStr = itemParser(board);
//        TextUtil.debugText("Inventory item:   " + invItemStr);
//        TextUtil.debugText("Board item:     " + boardItemStr);
        return invItemStr.equals(boardItemStr);
    }

    /* Parses items into an identifiable string and returns the string */
    public static String itemParser(ItemStack item) {
        String customModelString = getItemModelString(item);

        if (item.getType().equals(Material.ENCHANTED_BOOK) && !hasItemModelString(item)) {
            StringBuilder str = new StringBuilder();
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            for (Enchantment enchantment : meta.getStoredEnchants().keySet()) {
                str.append(enchantment.toString());
            }

            return item.getType() + str.toString();
        }

        if (item.getType().equals(Material.POTION) && !hasItemModelString(item)) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            return item.getType() + meta.getCustomEffects().toString();
        }

        return item.getType().name() + customModelString;
    }

    /* Gets the customModelString of an item returns "" if there is none */
    public static String getItemModelString(ItemStack item) {
        String customModelString;
        ItemMeta meta = item.getItemMeta();
        try {
            customModelString = meta.getCustomModelDataComponent().getStrings().getFirst();
        } catch (Exception ignored) {
            customModelString = "";
        }
        return customModelString;
    }

    /* Gets the customModelString of an item returns "" if there is none */
    public static boolean hasItemModelString(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta.hasCustomModelDataComponent()) {
            //TODO:Could cause future errors
            return !meta.getCustomModelDataComponent().getStrings().getFirst().isEmpty();
        }
        return false;
    }

    /*Checks to see if Player p is holding ItemStack item in their main hand*/
    public static boolean isHolding(Player p, ItemStack item) {
        return ItemComparison.isHoldingIn(p, item, EquipmentSlot.HAND);
    }

    /*Checks to see if Player p is holding ItemStack item in Equipment Slot equipment slot*/
    public static boolean isHoldingIn(Player p, ItemStack item, EquipmentSlot equipmentSlot) {
        ItemStack heldItem = p.getInventory().getItem(equipmentSlot);
        if (heldItem == null) return false;
        return ItemComparison.equalItems(heldItem, item);
    }
}
