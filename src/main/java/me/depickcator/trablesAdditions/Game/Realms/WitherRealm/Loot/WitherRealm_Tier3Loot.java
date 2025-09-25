package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot;

import me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet.KrivonBoots;
import me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet.KrivonChestplate;
import me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet.KrivonHelmet;
import me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet.KrivonLeggings;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.GrowthSprout;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.KrivonHandle;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ZombieHeart;
import me.depickcator.trablesAdditions.LootTables.Interfaces.CustomChestLoot;
import me.depickcator.trablesAdditions.LootTables.Interfaces.CustomChestLootPool;
import me.depickcator.trablesAdditions.LootTables.Interfaces.LootPoolItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class WitherRealm_Tier3Loot extends WitherRealmLoot {
    private final CustomChestLootPool rareBookPool;
    private final CustomChestLootPool tier3Pool;

    public WitherRealm_Tier3Loot() {
        rareBookPool = initRareBookPool();
        tier3Pool = initTier3Pool();
    }

    private CustomChestLootPool initRareBookPool() {
        CustomChestLootPool pool = new CustomChestLootPool(
                new LootPoolItem(getEnchantedBook(Enchantment.MENDING, 1), 1),
                new LootPoolItem(getEnchantedBook(Enchantment.LOOTING, 1), 2),
                new LootPoolItem(getEnchantedBook(Enchantment.PUNCH, 1), 2),
                new LootPoolItem(getEnchantedBook(Enchantment.INFINITY, 1), 1)
        );
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.PROTECTION, 2, 3, 3, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.UNBREAKING, 2, 3, 3, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.SHARPNESS, 2, 4, 3, 1));
        pool.addLootPoolItem(addEnchantedBooks(Enchantment.POWER, 2, 4, 3, 1));

        return pool;
    }

    private CustomChestLootPool initTier3Pool() {
        CustomChestLootPool pool = new CustomChestLootPool(
                new LootPoolItem(KrivonHandle.getInstance(), 8),
                new LootPoolItem(ZombieHeart.getInstance(), 25),
                new LootPoolItem(KrivonBoots.getInstance(), 15),
                new LootPoolItem(KrivonLeggings.getInstance(), 15),
                new LootPoolItem(KrivonChestplate.getInstance(), 8),
                new LootPoolItem(KrivonHelmet.getInstance(), 15),
                new LootPoolItem(GrowthSprout.getInstance(), 14)
        );

        return pool;
    }

    @Override
    public String getTierName() {
        return "Legendary";
    }

    @Override
    public Collection<ItemStack> populateLoot(Inventory inv, Random r, double luck) {
        Collection<ItemStack> items = new ArrayList<>();
        items.addAll(placeInInventory(inv, r, new ArrayList<>(rareBookPool.getRandomItemFromList(r, 2))));
        items.addAll(placeInInventory(inv, r, new ArrayList<>(tier3Pool.getRandomItemFromList(r, 1))));
        items.addAll(placeInInventory(inv, r, new ArrayList<>(initRareLootPool(r).getRandomItemFromList(r, 8, true))));
        return items;
    }

}
