package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot;

import me.depickcator.trablesAdditions.Game.Items.Uncraftable.RepairKit;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.ReviveStone;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.XPTome;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.CompactTNT;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ReinforcedPlating;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ShatteredQuiver;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.WitherRealmKey;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
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

    public abstract String getTierName();

    private CustomChestLootPool initCommonLootPool() {
        CustomChestLootPool lootPool = new CustomChestLootPool(
                new LootPoolItem(new ItemStack(Material.LEATHER), 15),
                new LootPoolItem(new ItemStack(Material.WHEAT), 25),
                new LootPoolItem(new ItemStack(Material.BUCKET), 20),
                new LootPoolItem(new ItemStack(Material.APPLE), 15),
                new LootPoolItem(new ItemStack(Material.MELON_SEEDS), 20),
                new LootPoolItem(new ItemStack(Material.PUMPKIN_SEEDS), 20),
                new LootPoolItem(new ItemStack(Material.BEETROOT_SEEDS), 20),
                new LootPoolItem(new ItemStack(Material.TNT), 3),
                new LootPoolItem(new ItemStack(Material.IRON_HELMET), 1),
                new LootPoolItem(new ItemStack(Material.IRON_CHESTPLATE), 1),
                new LootPoolItem(new ItemStack(Material.IRON_LEGGINGS), 1),
                new LootPoolItem(new ItemStack(Material.IRON_BOOTS), 1),
                new LootPoolItem(CompactTNT.getInstance().getResult(), 2),
                new LootPoolItem(RepairKit.getInstance().getResult(), 2),
                new LootPoolItem(new ItemStack(Material.COOKED_BEEF, 8), 5),
                new LootPoolItem(new ItemStack(Material.COOKED_PORKCHOP, 4), 10),
                new LootPoolItem(new ItemStack(Material.COOKED_COD, 8), 10),
                new LootPoolItem(new ItemStack(Material.CARROT, 3), 10)
        );
        lootPool.generateItems(Material.GUNPOWDER, 1, 2, 1, 7);
        lootPool.generateItems(Material.SAND, 1, 2, 1, 5);
        lootPool.generateItems(Material.COAL, 1, 2, 1, 10);
        lootPool.generateItems(Material.REDSTONE, 1, 2, 1, 10);
        lootPool.generateItems(Material.GOLD_INGOT, 1, 2, 1, 7);
        lootPool.generateItems(Material.IRON_INGOT, 1, 2, 1, 8);
        lootPool.generateItems(Material.DIAMOND, 1, 2, 1, 2);
        lootPool.generateItems(Material.OAK_PLANKS, 16, 2, 16, 2);
        lootPool.generateItems(Material.SPRUCE_PLANKS, 16, 2, 16, 2);
        lootPool.generateItems(Material.DARK_OAK_PLANKS, 16, 2, 16, 2);
        lootPool.generateItems(Material.BIRCH_PLANKS, 16, 2, 16, 2);
        lootPool.generateItems(Material.JUNGLE_PLANKS, 16, 2, 16, 2);
        return lootPool;
    }

    private CustomChestLootPool initBookPools() {
        CustomChestLootPool pool = new CustomChestLootPool(
                new LootPoolItem(getEnchantedBook(Enchantment.MENDING, 1), 1),
                new LootPoolItem(getEnchantedBook(Enchantment.LOOTING, 1), 2),
                new LootPoolItem(getEnchantedBook(Enchantment.PUNCH, 1), 2),
                new LootPoolItem(getEnchantedBook(Enchantment.INFINITY, 1), 1)
        );
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.PROTECTION, 1, 3, 1, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.UNBREAKING, 1, 3, 1, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.SHARPNESS, 1, 3, 1, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.POWER, 1, 3, 1, 1));

        pool.addLootPoolItem(addEnchantedBooks(Enchantment.PROJECTILE_PROTECTION, 1, 3, 1, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.KNOCKBACK, 1, 2, 1, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.BANE_OF_ARTHROPODS, 1, 3, 1, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.SMITE, 1, 3, 1, 1));

        pool.addLootPoolItem(addEnchantedBooks(Enchantment.AQUA_AFFINITY, 1, 1, 1, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.RESPIRATION, 1, 3, 1, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.BLAST_PROTECTION, 1, 3, 1, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.FIRE_PROTECTION, 1, 3, 1, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.EFFICIENCY, 1, 3, 1, 1));
        return pool;
    }

    protected CustomChestLootPool initRareLootPool(Random r) {
        CustomChestLootPool pool = new CustomChestLootPool(
                new LootPoolItem(RepairKit.getInstance(), 15), //15
                new LootPoolItem(ReviveStone.getInstance(), 12), //12
                new LootPoolItem(ReinforcedPlating.getInstance(), 4),//4
                new LootPoolItem(ShatteredQuiver.getInstance(), 4), //4
                new LootPoolItem(CompactTNT.getInstance(), 4), //4
                new LootPoolItem(WitherRealmKey.getInstance(), 2), //1
                new LootPoolItem(XPTome.getInstance().getResult(500), 2)
        );
        pool.generateItems(Material.GOLDEN_APPLE, 1, 3, 1, 4); //12
        pool.generateItems(Material.ARROW, 8, 4, 8, 5); //20
        for (ItemStack book : bookLootPool.getRandomItemFromList(r, 2).stream().toList()) {
            pool.addLootPoolItem(new LootPoolItem(book, 5));
        }
        pool.generateItems(Material.AIR, 1, 5, 0, 50);
        return pool;
    }

    protected ItemStack getEnchantedBook(Enchantment enchantment, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        book.setItemMeta(meta);
        return book;
    }

    protected List<LootPoolItem> addEnchantedBooks(Enchantment enchant, int minLvl, int maxLvl,
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
