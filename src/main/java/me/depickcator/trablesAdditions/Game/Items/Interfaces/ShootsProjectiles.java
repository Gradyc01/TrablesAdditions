package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public interface ShootsProjectiles {
//    String METADATA = "projectiles";
    static NamespacedKey key = new NamespacedKey(TrablesAdditions.getInstance(), "projectiles");
//    NamespacedKey key = new NamespacedKey("minecraft", METADATA);
    Map<String, ShootsProjectiles> projectiles = new HashMap<>();
    void applyKey(EntityShootBowEvent event, ItemStack weapon);
    default void addProjectile(CustomItem customItem, ShootsProjectiles projectile) {
        projectiles.put(ItemComparison.itemParser(customItem.getResult()), projectile);
    }

    /*Sets any projectile component that needs to be set.
    Returns the new damage value of the event, -1 if not want it to change*/
    double setProjectileComponent(EntityDamageByEntityEvent event, LivingEntity victim);
    boolean onHit(ProjectileHitEvent event);

    static ShootsProjectiles getProjectile(ItemStack item) {
//        TextUtil.debugText(ItemComparison.itemParser(item));
        return projectiles.get(ItemComparison.itemParser(item));
    }

    static ShootsProjectiles getProjectileByEntity(Projectile entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        String string = container.get(key, PersistentDataType.STRING);
        if (string == null) return null;
        return projectiles.get(string);
    }

    static void applyProjectileKey(Projectile entity, ItemStack item) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, ItemComparison.itemParser(item));
    }
}
