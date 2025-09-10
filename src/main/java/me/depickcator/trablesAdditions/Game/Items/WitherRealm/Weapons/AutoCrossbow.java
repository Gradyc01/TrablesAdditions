package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Effects.GolemLaunch;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.*;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ShatteredQuiver;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class AutoCrossbow extends AutomaticCrossbows {
    private static AutoCrossbow instance;
    private AutoCrossbow() {
        super("Auto Crossbow", "auto_crossbow", 8);
        addProjectile(this, this);
    }

    public static AutoCrossbow getInstance() {
        if (instance == null) instance = new AutoCrossbow();
        return instance;
    }

    @Override
    protected Recipe initRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, getKey());
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("ABA", "ACA", "AAA");
        recipe.setIngredient('A', Material.ARROW);
        recipe.setIngredient('B', ShatteredQuiver.getInstance().getResult());
        recipe.setIngredient('C', Material.CROSSBOW);
        return recipe;
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.CROSSBOW);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName()));
        meta.setMaxDamage(256);
        item.setItemMeta(meta);
        addCooldownGroup(item);
        generateUniqueModelString(item);
        return item;
    }
}
