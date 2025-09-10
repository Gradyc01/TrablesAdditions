package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Effects.NatureWrath;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ShootsProjectiles;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.KrivonHandle;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.OceanCore;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ThunderCore;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.List;

public class Poseidon extends Craft implements ShootsProjectiles {
    private static Poseidon instance;
    private Poseidon() {
        super("Poseidon", "poseidon");
        addProjectile(this, this);
    }

    protected Recipe initRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, getKey());

        ShapedRecipe recipe = new ShapedRecipe(key, getResult());
        recipe.shape("A A", "BCB", " C ");
        recipe.setIngredient('A', ThunderCore.getInstance().getResult());
        recipe.setIngredient('B', OceanCore.getInstance().getResult());
        recipe.setIngredient('C', KrivonHandle.getInstance().getResult());
        return recipe;
    }

    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.TRIDENT);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.YELLOW));
        meta.addEnchant(Enchantment.LOYALTY, 1, true);
        meta.setMaxDamage(32);
        List<Component> lore = new ArrayList<>(List.of(
                TextUtil.makeText("Smites anything that it touches", TextUtil.DARK_PURPLE)
        ));
        meta.lore(lore);
        item.setItemMeta(meta);
        addCooldownGroup(item, 13);
        generateUniqueModelString(item);
        return item;
    }

    @Override
    public void applyKey(EntityShootBowEvent event, ItemStack weapon) {
        TextUtil.debugText("Poseidon", "Fired");
    }

    @Override
    public double setProjectileComponent(EntityDamageByEntityEvent event, LivingEntity victim) {
        LivingEntity attacker = ((LivingEntity) event.getDamageSource().getCausingEntity());
        double distance = victim.getLocation().distance(attacker.getLocation());
        new NatureWrath(victim, Integer.min(8, Integer.max(3, (int) distance/5)));
        return -1;
    }

    @Override
    public boolean onHit(ProjectileHitEvent event) {
        return true;
    }

    public static Poseidon getInstance() {
        if (instance == null) instance = new Poseidon();
        return instance;
    }
}
