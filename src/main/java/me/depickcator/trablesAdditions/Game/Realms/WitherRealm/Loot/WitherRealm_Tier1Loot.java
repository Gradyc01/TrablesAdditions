package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
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
