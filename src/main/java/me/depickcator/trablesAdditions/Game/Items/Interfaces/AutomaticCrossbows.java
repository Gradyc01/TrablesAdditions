package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ShatteredQuiver;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons.AutoCrossbow;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons.CupidBow;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;

public abstract class AutomaticCrossbows extends Craft implements ShootsProjectiles {
    private final float cooldownTime;
    public AutomaticCrossbows(String displayName, String key, float seconds) {
        super(displayName, key);
        this.cooldownTime = seconds;
    }

    @Override
    public void applyKey(EntityShootBowEvent event, ItemStack weapon) {
        if (!(event.getEntity() instanceof Player player)) return;
        player.setCooldown(weapon, (int) (cooldownTime * 20));
        PlayerInventory inv = player.getInventory();
        if (inv.contains(Material.ARROW)) {
            ItemStack item = inv.getItem(inv.first(Material.ARROW));
            item.setAmount(item.getAmount() - 1);
            CrossbowMeta bowMeta = (CrossbowMeta) weapon.getItemMeta();
            bowMeta.addChargedProjectile(item);
            weapon.setItemMeta(bowMeta);
        }
    }

    @Override
    public double setProjectileComponent(EntityDamageByEntityEvent event, LivingEntity victim) {
        return -1;
    }

    @Override
    public boolean onHit(ProjectileHitEvent event) {
        return true;
    }

}
