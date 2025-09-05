package me.depickcator.trablesAdditions.Game.Items;

import me.depickcator.trablesAdditions.Game.Items.Crafts.MinerBlessing;
import me.depickcator.trablesAdditions.Game.Items.Crafts.QuickPick;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.HashMap;
import java.util.Map;

public class CraftData {
    private final Map<NamespacedKey, Craft> crafts; /*The recipeKey (recipe.getKey()) paired with the craft itself*/
    private final TrablesAdditions plugin;
    public CraftData() {
        crafts = new HashMap<>();
        plugin = TrablesAdditions.getInstance();
    }

    public void registerCraft(Craft craft) {
        Recipe recipe = craft.getRecipe();
        if (recipe instanceof ShapedRecipe shaped) {
            shaped.setCategory(CraftingBookCategory.EQUIPMENT);
            crafts.put(shaped.getKey(), craft);
        }
        if (recipe instanceof ShapelessRecipe shapeless) {
            shapeless.setCategory(CraftingBookCategory.EQUIPMENT);
            crafts.put(shapeless.getKey(), craft);
        }
        plugin.getServer().addRecipe(recipe);
        TextUtil.debugText("CraftsData", "Server registered craft " + craft.getDisplayName());
    }

    public Craft findCraft(NamespacedKey recipeKey) {
        return crafts.get(recipeKey);
    }

    public void initCrafts() {
        QuickPick.getInstance();
        MinerBlessing.getInstance();
    }
}
