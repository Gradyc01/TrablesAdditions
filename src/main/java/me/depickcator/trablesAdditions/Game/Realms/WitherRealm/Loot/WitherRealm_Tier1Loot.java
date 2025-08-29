package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot;

import me.depickcator.trablesAdditions.Game.Items.Uncraftable.RepairKit;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.ReviveStone;
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
import java.util.List;
import java.util.Random;

public class WitherRealm_Tier1Loot extends WitherRealmLoot {
    public WitherRealm_Tier1Loot() {
    }



    @Override
    public Collection<ItemStack> populateLoot(Inventory inv, Random r, double luck) {
        Collection<ItemStack> items = new ArrayList<>();
        items.addAll(placeInInventory(inv, r, new ArrayList<>(commonLootPool.getRandomItemFromList(r, 15, 1, 2))));
        items.addAll(placeInInventory(inv, r, new ArrayList<>(initRareLootPool(r).getRandomItemFromList(r, 2, true))));
        return items;
    }

}
