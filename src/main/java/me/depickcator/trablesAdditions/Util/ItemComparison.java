package me.depickcator.trablesAdditions.Util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
        int customModelNumber = getItemModelNumber(item);

        if (item.getType().equals(Material.ENCHANTED_BOOK) && !item.getItemMeta().hasCustomModelData()) {
            StringBuilder str = new StringBuilder();
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            for (Enchantment enchantment : meta.getStoredEnchants().keySet()) {
                str.append(enchantment.toString());
            }

            return item.getType() + str.toString();
        }

        if (item.getType().equals(Material.POTION) && !item.getItemMeta().hasCustomModelData()) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            return item.getType() + meta.getCustomEffects().toString();
        }

        return item.getType().toString() + customModelNumber;
    }

    /* Gets the customModelNumber of an item returns 0 if there is none */
    public static int getItemModelNumber(ItemStack item) {
        int customModelNumber;
        ItemMeta meta = item.getItemMeta();
        try {
            customModelNumber = meta.getCustomModelData();
        } catch (Exception ignored) {
            customModelNumber = 0;
        }
        return customModelNumber;
    }
}
