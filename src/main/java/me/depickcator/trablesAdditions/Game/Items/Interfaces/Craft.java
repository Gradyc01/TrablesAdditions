package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public abstract class Craft extends CustomItem {
    protected Recipe recipe;

    protected Craft(String displayName, String key) {
        this(displayName, key, false);
    }

    protected Craft(String displayName, String key, boolean removeVanillaRecipe) {
        super(displayName, key);
        if (removeVanillaRecipe) removeVanillaRecipe();
        this.recipe = initRecipe();
        plugin.getCraftData().registerCraft(this);
    }

    /*Can have a certain affect when upon Crafted*/
    public boolean uponCrafted(CraftItemEvent e, PlayerData pD) {
        return true;
    }

    /*Initializes the Recipe and returns the Recipe that is initialized*/
    protected abstract Recipe initRecipe();
    protected abstract ItemStack initResult();

    protected NamespacedKey generateKey() {
        return new NamespacedKey(plugin, getKey());
    }

    private void removeVanillaRecipe() {
        plugin.getServer().removeRecipe(NamespacedKey.minecraft(getKey()));
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
