package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.CompactTNT;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.GrowthSprout;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ShatteredQuiver;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ZombieHeart;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AdvanceGrowthCraft extends Craft {
    private static AdvanceGrowthCraft instance;

    private AdvanceGrowthCraft() {
        super("Soul Growth", "soul_growth_craft");
    }


    @Override
    protected Recipe initRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, getKey());
        ShapedRecipe recipe = new ShapedRecipe(key, getResult());
        recipe.shape("A A", " B ", "A A");
        recipe.setIngredient('A', ZombieHeart.getInstance().getResult());
        recipe.setIngredient('B', GrowthSprout.getInstance().getResult());
        return recipe;
    }

    @Override
    protected ItemStack initResult() {
        return AdvancedGrowth.getInstance().getResult();
    }

    public static AdvanceGrowthCraft getInstance() {
        if (instance == null) instance = new AdvanceGrowthCraft();
        return instance;
    }



}
