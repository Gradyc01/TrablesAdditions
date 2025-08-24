package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot;

import me.depickcator.trablesAdditions.LootTables.Interfaces.CustomChestLoot;
import me.depickcator.trablesAdditions.LootTables.Interfaces.CustomChestLootPool;
import me.depickcator.trablesAdditions.LootTables.Interfaces.LootPoolItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class WitherRealm_Tier1Loot extends CustomChestLoot {
    private final CustomChestLootPool lootPool;

    public WitherRealm_Tier1Loot() {
        this.lootPool = initLootPool();
    }

    private CustomChestLootPool initLootPool() {
        return new CustomChestLootPool(
                new LootPoolItem(new ItemStack(Material.ARROW, 8), 20),
                new LootPoolItem(new ItemStack(Material.WHEAT, 8), 20),
                new LootPoolItem(new ItemStack(Material.APPLE, 8), 20),
                new LootPoolItem(new ItemStack(Material.STRING, 8), 20),
                new LootPoolItem(new ItemStack(Material.ARROW, 16), 15),
                new LootPoolItem(new ItemStack(Material.WHEAT, 8), 15),
                new LootPoolItem(new ItemStack(Material.APPLE, 8), 15),
                new LootPoolItem(new ItemStack(Material.STRING, 8), 15),
                new LootPoolItem(new ItemStack(Material.ARROW, 32), 10),
                new LootPoolItem(new ItemStack(Material.WHEAT, 8), 10),
                new LootPoolItem(new ItemStack(Material.APPLE, 8), 10),
                new LootPoolItem(new ItemStack(Material.STRING, 8), 10),
                new LootPoolItem(getEnchantedBook(Enchantment.LOOTING, 1), 5),
                new LootPoolItem(getEnchantedBook(Enchantment.LOOTING, 2), 3),
                new LootPoolItem(getEnchantedBook(Enchantment.LOOTING, 3), 1),
                new LootPoolItem(getEnchantedBook(Enchantment.SHARPNESS, 1), 5),
                new LootPoolItem(getEnchantedBook(Enchantment.SHARPNESS, 2), 3),
                new LootPoolItem(getEnchantedBook(Enchantment.SHARPNESS, 3), 1),
                new LootPoolItem(getEnchantedBook(Enchantment.POWER, 1), 5),
                new LootPoolItem(getEnchantedBook(Enchantment.POWER, 2), 3),
                new LootPoolItem(getEnchantedBook(Enchantment.POWER, 3), 1)
        );
    }

    private ItemStack getEnchantedBook(Enchantment enchantment, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        book.setItemMeta(meta);
        return book;
    }

    @Override
    public Collection<ItemStack> populateLoot(Inventory inv, Random r, double luck) {
        return placeInInventory(inv, r, new ArrayList<>(lootPool.getRandomItemFromList(r, 3, true)));
    }
}
