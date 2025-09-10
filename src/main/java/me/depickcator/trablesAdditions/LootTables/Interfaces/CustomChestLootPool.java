package me.depickcator.trablesAdditions.LootTables.Interfaces;

import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CustomChestLootPool {
    private int totalWeight;
    private final List<LootPoolItem> lootPool;

    public CustomChestLootPool(LootPoolItem... lootPoolItems) {
        this.totalWeight = 0;
        this.lootPool = new ArrayList<>();
        addLootPoolItem(lootPoolItems);
    }

    public CustomChestLootPool() {
        this.totalWeight = 0;
        this.lootPool = new ArrayList<>();
    }

    /*Adds Loot pool items to the CustomChestLootPool*/
    public void addLootPoolItem(LootPoolItem... lootPoolItems) {
        addLootPoolItem(Arrays.asList(lootPoolItems));
    }

    /*Adds Loot pool items to the CustomChestLootPool*/
    public void addLootPoolItem(List<LootPoolItem> lootPoolItems) {
        for (LootPoolItem lootPoolItem : lootPoolItems) {
            this.lootPool.add(lootPoolItem);
            this.totalWeight+= lootPoolItem.getWeight();
        }
    }



    public Collection<ItemStack> getRandomItemFromList(Random r, int count) {
        return getRandomItemFromList(r, count, false);
    }

    public Collection<ItemStack> getRandomItemFromList(Random r, int count, boolean uniqueItems) {
        return getRandomItemFromList(r, count, 1, 1, false, uniqueItems);
    }

    public Collection<ItemStack> getRandomItemFromList(Random r, int count, int min, int max) {
        return getRandomItemFromList(r, count, min, max, true, false);
    }


    /*Generate num number of items with the first stack being base amount and then incrementing up by increment*/
    public void generateItems(Material material, int base, int num, int increment, int weight) {
        for (int i = 0; i < num; i++) {
            ItemStack item = new ItemStack(material, base + i * increment);
            addLootPoolItem(new LootPoolItem(item, weight));
        }
    }

    public void generateItems(Material material, int base, int num, int increment) {
        generateItems(material, base, num, increment, 1);
    }

    /* Gets a number of random items from arr and randomly sets the amount between min and max if changeItemAmount is true
     * Returns a Collection of ItemStacks*/
    private Collection<ItemStack> getRandomItemFromList(Random r, int count, int min, int max, boolean changeItemAmount, boolean uniqueItems) {
        int totalWeight = this.totalWeight;
        List<LootPoolItem> items = new ArrayList<>(lootPool);
        List<ItemStack> ans = new ArrayList<>();
        if (uniqueItems && count > items.size()) {
            throw new IllegalArgumentException("Not enough unique items to satisfy the count.");
        }
        for (int i = 0; i < count; i++) {
            int weight = r.nextInt(totalWeight);
//            TextUtil.debugText("Random Weight number is " + weight);
            LootPoolItem lootPoolItem = getWeightedItem(items, weight);
            if (lootPoolItem == null) throw new IllegalArgumentException("Illegal argument too much weight");

            ItemStack itemStack = lootPoolItem.getItem();
            if (changeItemAmount) {
                itemStack.setAmount(Math.min(r.nextInt(max - min + min) + 1, itemStack.getMaxStackSize()));
            }
            ans.add(itemStack);

            if (uniqueItems) {
                totalWeight -= lootPoolItem.getWeight(); // Update totalWeight
                items.remove(lootPoolItem); // Remove selected item to prevent reuse
            }
        }
        return ans;
    }



    /*Get the item that falls into the weight specified
     * Returns the item or null if it doesn't find one*/
    private LootPoolItem getWeightedItem(List<LootPoolItem> items, int weight) {
        int indexWeight = 0;
        String debugString = "Obtaining Process: ";
        for (LootPoolItem item : items) {
            indexWeight += item.getWeight();
            debugString += indexWeight + " --> ";
            if (indexWeight > weight) {
//                TextUtil.debugText(debugString + "Item is " + item.getItem().getType().name());
                return item;
            }
        }
        return null;
    }

    public List<ItemStack> getAllItemsInPool() {
        List<ItemStack> items = new ArrayList<>();
        for (LootPoolItem item : lootPool) {
            items.add(item.getItem());
        }
        return items;
    }
}
