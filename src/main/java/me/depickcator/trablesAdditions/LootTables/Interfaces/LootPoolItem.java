package me.depickcator.trablesAdditions.LootTables.Interfaces;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LootPoolItem {
    private final ItemStack item;
    private final int weight;
    public LootPoolItem(ItemStack item, int weight) {
        this.item = item;
        this.weight = weight;
    }

    public LootPoolItem(ItemStack item) {
        this(item, 1);
    }

    public LootPoolItem(Material material, int weight) {
        this(new ItemStack(material), weight);
    }

    public LootPoolItem(Material material) {
        this(new ItemStack(material), 1);
    }

    public ItemStack getItem() {
        return item;
    }

    public int getWeight() {
        return weight;
    }
}
