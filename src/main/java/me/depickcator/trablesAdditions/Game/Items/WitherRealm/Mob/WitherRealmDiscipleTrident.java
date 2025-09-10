package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Mob;

import me.depickcator.trablesAdditions.Game.Effects.NatureWrath;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ShootsProjectiles;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class WitherRealmDiscipleTrident extends CustomItem implements ShootsProjectiles {
    private static WitherRealmDiscipleTrident instance;
    private WitherRealmDiscipleTrident() {
        super("Disciple's Staff", "wither_realm_disciple_trident");
        addProjectile(this, this);
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.TRIDENT);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.setMaxDamage(32);
        meta.setEnchantmentGlintOverride(true);
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.YELLOW));
        meta.addEnchant(Enchantment.LOYALTY, 1, true);
//        meta.getPersistentDataContainer().set(ShootsProjectiles.key, PersistentDataType.STRING, getKey());
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    public static WitherRealmDiscipleTrident getInstance() {
        if (instance == null) instance = new WitherRealmDiscipleTrident();
        return instance;
    }

    @Override
    public void applyKey(EntityShootBowEvent event, ItemStack weapon) {

    }

    @Override
    public double setProjectileComponent(EntityDamageByEntityEvent event, LivingEntity victim) {
        new NatureWrath(victim, 3);
        return -1;
    }

    @Override
    public boolean onHit(ProjectileHitEvent event) {
        return true;
    }
}
