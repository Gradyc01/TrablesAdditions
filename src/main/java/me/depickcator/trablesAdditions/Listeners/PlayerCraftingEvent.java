package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class PlayerCraftingEvent extends TrablesListeners {

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        Recipe recipe = event.getRecipe();
        if (!isCraftingRecipe(recipe)) return;
        NamespacedKey recipeKey = getKey(recipe);
        craftingCustomItemEvent(event, recipeKey);
    }

    private void craftingCustomItemEvent(CraftItemEvent event, NamespacedKey recipeKey) {
        Player player = (Player) event.getWhoClicked();
        Craft c = TrablesAdditions.getInstance().getCraftData().findCraft(recipeKey);
        if (c == null) return;
        if (!c.uponCrafted(event, PlayerUtil.getPlayerData(player))) {
            event.setCancelled(true);
        }
    }

    /*Gets the recipe key for Recipe recipe*/
    private NamespacedKey getKey(Recipe recipe) {
        if (recipe instanceof ShapedRecipe) {
            return ((ShapedRecipe) recipe).getKey();
        }
        if (recipe instanceof ShapelessRecipe) {
            return ((ShapelessRecipe) recipe).getKey();
        }
        return null;
    }

    /*Checks if it is a crafting recipe*/
    private boolean isCraftingRecipe(Recipe recipe) {
        if (recipe == null) {
            return false;
        }
        return recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe;
    }


}
