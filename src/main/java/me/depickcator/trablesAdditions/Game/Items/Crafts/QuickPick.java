package me.depickcator.trablesAdditions.Game.Items.Crafts;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Repairable;

public class QuickPick extends Craft {
    private static QuickPick instance;
    private QuickPick() {
        super("Quick Pick", "quick_pick");
    }

    public static QuickPick getInstance() {
        if (instance == null) instance = new QuickPick();
        return instance;
    }

    @Override
    protected Recipe initRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(generateKey(), getResult());
        recipe.shape("AAA", "BCB", " C ");
        recipe.setIngredient('A', Material.RAW_IRON);
        recipe.setIngredient('B', Material.COAL);
        recipe.setIngredient('C', Material.STICK);
        return recipe;
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.IRON_PICKAXE);
        Repairable meta = (Repairable) item.getItemMeta();
        Component name = TextUtil.makeText(getDisplayName(), TextUtil.AQUA);
        meta.displayName(name);
        meta.addEnchant(Enchantment.EFFICIENCY, 1, true);
        meta.setRepairCost(999);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }
}
