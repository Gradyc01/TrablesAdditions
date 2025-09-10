package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.Weapon;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.KrivonHandle;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ZombieHeart;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;

import java.util.List;

public class ReaperScythe extends Weapon {
    private static ReaperScythe instance;

    protected ReaperScythe() {
        super("Reaper Scythe","reaper_scythe", 5, -2.4);
    }

    @Override
    public boolean onCustomWeaponUse(EntityDamageByEntityEvent event, LivingEntity attacker, LivingEntity target) {
        double finalDamage = event.getFinalDamage();
        attacker.heal(finalDamage/10);
        attacker.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, attacker.getLocation(), 15, 3, 2,3);
        return true;
    }

    @Override
    protected Recipe initRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(generateKey(), getResult());
        recipe.shape("BB ", " A ", " A ");
        recipe.setIngredient('A', KrivonHandle.getInstance().getResult());
        recipe.setIngredient('B', ZombieHeart.getInstance().getResult());
        return recipe;
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.DIAMOND_HOE);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.addEnchant(Enchantment.SMITE, 6, true);
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.PINK));
        meta.lore(List.of(
                TextUtil.makeText("Regain damage dealt as health", TextUtil.DARK_PURPLE)
        ));
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    public static ReaperScythe getInstance() {
        if (instance == null) instance = new ReaperScythe();
        return instance;
    }

}
