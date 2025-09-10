package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.AutomaticCrossbows;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemCooldown;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ShootsProjectiles;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.CupidEssence;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ShatteredQuiver;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;

public class CupidBow extends AutomaticCrossbows {
    private static CupidBow instance;
    private CupidBow() {
        super("Cupid's Bow", "cupid_bow", 8);
        addProjectile(this, this);
    }

    public static CupidBow getInstance() {
        if (instance == null) instance = new CupidBow();
        return instance;
    }

    @Override
    protected Recipe initRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, getKey()), result);
        recipe.shape(" C ", "ABA", " D ");
        recipe.setIngredient('A', CupidEssence.getInstance().getResult());
        recipe.setIngredient('B', AutoCrossbow.getInstance().getResult());
        recipe.setIngredient('C', Material.BLAZE_ROD);
        recipe.setIngredient('D', Material.LAVA_BUCKET);
        return recipe;
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.CROSSBOW);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.addEnchant(Enchantment.POWER, 2, true);
        meta.addEnchant(Enchantment.FLAME, 1, true);
        meta.setMaxStackSize(1);
        meta.setMaxDamage(192);
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.DARK_RED));
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        addCooldownGroup(item);
        return item;
    }
}
