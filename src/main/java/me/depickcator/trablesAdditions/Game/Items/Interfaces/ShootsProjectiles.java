package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public interface ShootsProjectiles {
    String METADATA = "projectiles";
    NamespacedKey key = new NamespacedKey("minecraft", METADATA);
    Map<String, ShootsProjectiles> projectiles = new HashMap<>();
    void applyKey(EntityShootBowEvent event);
    default void addProjectile(String key, ShootsProjectiles projectile) {
        projectiles.put(key, projectile);
    }

    /*Sets any projectile component that needs to be set.
    Returns the new damage value of the event, -1 if not want it to change*/
    double setProjectileComponent(EntityDamageByEntityEvent event, LivingEntity victim);
    static ShootsProjectiles getProjectile(MetadataValue metadataValue) {
//        Bukkit.getServer().broadcast(TextUtil.makeText(metadataValue.asString()));
        return projectiles.get(metadataValue.asString());
    }
    static ShootsProjectiles getProjectile(ItemStack item) {
        try {
            return projectiles.get(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING));
        } catch (Exception e) {
            return null;
        }

    }
}
