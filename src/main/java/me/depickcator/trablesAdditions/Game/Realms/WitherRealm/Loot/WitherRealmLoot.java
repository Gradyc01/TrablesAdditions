package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot;

import me.depickcator.trablesAdditions.Game.Items.Uncraftable.RepairKit;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.ReviveStone;
import me.depickcator.trablesAdditions.LootTables.Interfaces.CustomChestLoot;
import me.depickcator.trablesAdditions.LootTables.Interfaces.CustomChestLootPool;
import me.depickcator.trablesAdditions.LootTables.Interfaces.LootPoolItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class WitherRealmLoot extends CustomChestLoot {
    protected final CustomChestLootPool commonLootPool;
    protected final CustomChestLootPool bookLootPool;

    public WitherRealmLoot() {
        this.bookLootPool = initBookPools();
        this.commonLootPool = initCommonLootPool();
    }

    private CustomChestLootPool initCommonLootPool() {
        return new CustomChestLootPool(
                new LootPoolItem(new ItemStack(Material.LEATHER), 15),
                new LootPoolItem(new ItemStack(Material.WHEAT), 25),
                new LootPoolItem(new ItemStack(Material.BUCKET), 20),
                new LootPoolItem(new ItemStack(Material.APPLE), 15),
                new LootPoolItem(new ItemStack(Material.GUNPOWDER), 15),
                new LootPoolItem(new ItemStack(Material.SAND), 10),
                new LootPoolItem(new ItemStack(Material.MELON_SEEDS), 20),
                new LootPoolItem(new ItemStack(Material.PUMPKIN_SEEDS), 20),
                new LootPoolItem(new ItemStack(Material.COAL), 25),
                new LootPoolItem(new ItemStack(Material.REDSTONE), 25),
                new LootPoolItem(new ItemStack(Material.BEETROOT_SEEDS), 20),
                new LootPoolItem(new ItemStack(Material.GOLD_INGOT), 15),
                new LootPoolItem(new ItemStack(Material.IRON_INGOT), 15),
                new LootPoolItem(new ItemStack(Material.DIAMOND), 5),
                new LootPoolItem(new ItemStack(Material.IRON_HELMET), 1),
                new LootPoolItem(new ItemStack(Material.IRON_CHESTPLATE), 1),
                new LootPoolItem(new ItemStack(Material.IRON_LEGGINGS), 1),
                new LootPoolItem(new ItemStack(Material.IRON_BOOTS), 1),
                new LootPoolItem(new ItemStack(Material.COOKED_BEEF), 12),
                new LootPoolItem(new ItemStack(Material.OAK_PLANKS), 15),
                new LootPoolItem(new ItemStack(Material.CARROT), 10)
        );
    }

    private CustomChestLootPool initBookPools() {
        CustomChestLootPool pool = new CustomChestLootPool(
                new LootPoolItem(getEnchantedBook(Enchantment.MENDING, 1), 1),
                new LootPoolItem(getEnchantedBook(Enchantment.LOOTING, 1), 2),
                new LootPoolItem(getEnchantedBook(Enchantment.PUNCH, 1), 2),
                new LootPoolItem(getEnchantedBook(Enchantment.INFINITY, 1), 1)
        );
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.PROTECTION, 1, 3, 5, 5));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.UNBREAKING, 1, 3, 5, 5));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.SHARPNESS, 1, 3, 3, 3));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.POWER, 1, 3, 3, 3));

        pool.addLootPoolItem(addEnchantedBooks(Enchantment.PROJECTILE_PROTECTION, 1, 3, 7, 5));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.KNOCKBACK, 1, 3, 7, 5));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.BANE_OF_ARTHROPODS, 1, 3, 7, 5));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.SMITE, 1, 3, 7, 5));

        pool.addLootPoolItem(addEnchantedBooks(Enchantment.AQUA_AFFINITY, 1, 1, 10, 3));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.RESPIRATION, 1, 3, 10, 3));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.BLAST_PROTECTION, 1, 3, 10, 3));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.FIRE_PROTECTION, 1, 3, 10, 3));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.EFFICIENCY, 1, 3, 10, 3));
        return pool;
    }

    protected CustomChestLootPool initRareLootPool(Random r) {
        CustomChestLootPool pool = new CustomChestLootPool(
                new LootPoolItem(RepairKit.getInstance().getResult(), 14), //14
                new LootPoolItem(ReviveStone.getInstance().getResult(), 10) //10
        );
        pool.generateItems(Material.GOLDEN_APPLE, 1, 3, 1, 4); //12
        pool.generateItems(Material.ARROW, 8, 4, 8, 5); //20
        for (ItemStack book : bookLootPool.getRandomItemFromList(r, 2).stream().toList()) {
            pool.addLootPoolItem(new LootPoolItem(book, 5));
        }
        pool.generateItems(Material.AIR, 1, 5, 0, 50);
        return pool;
    }

    private ItemStack getEnchantedBook(Enchantment enchantment, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        book.setItemMeta(meta);
        return book;
    }

    private List<LootPoolItem> addEnchantedBooks(Enchantment enchant, int minLvl, int maxLvl,
                                                 int startWeight, int weightInc) {
        List<LootPoolItem> items = new ArrayList<>();
        int weight = startWeight;
        for (int level = maxLvl; level >= minLvl; level--) {
            items.add(new LootPoolItem(getEnchantedBook(enchant, level), weight));
            weight += weightInc;
        }
        return items;
    }
}
