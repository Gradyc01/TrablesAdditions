package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.ShootsProjectiles;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class ProjectileLaunch extends TrablesListeners {
    private final Set<Material> projectiles = Set.of(Material.BOW, Material.CROSSBOW, Material.TRIDENT,
            Material.SNOWBALL, Material.EGG, Material.ENDER_PEARL, Material.SPLASH_POTION, Material.LINGERING_POTION);
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        ProjectileSource source = projectile.getShooter();
        fixProjectileBug(projectile, source);
//        ItemStack weapon = findShooterItem(source);
//        if (weapon == null) return;
//        TextUtil.debugText("Weapon " + TextUtil.getComponentString(weapon.displayName()));

    }

    @EventHandler
    public void onShootProjectile(EntityShootBowEvent event) {
        ItemStack weapon = event.getBow();
        Projectile projectile = (Projectile) event.getProjectile();
        ShootsProjectiles customProjectile = ShootsProjectiles.getProjectile(weapon);
        if (customProjectile != null) {
            ShootsProjectiles.applyProjectileKey(projectile, weapon);
            customProjectile.applyKey(event, weapon);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
//        if (projectile.isEmpty()) return;
        ShootsProjectiles customProjectile = ShootsProjectiles.getProjectileByEntity(projectile);
        if (customProjectile != null) {
            event.setCancelled(!customProjectile.onHit(event));
        }
    }

    private void fixProjectileBug(Projectile projectile, ProjectileSource source) {
        if (source instanceof Player player && player.canSee(projectile)) {
            player.hideEntity(TrablesAdditions.getInstance(), projectile);
            TrablesAdditions.getInstance().getServer().getScheduler()
                    .runTaskLater(TrablesAdditions.getInstance(),
                            bukkitTask -> player.showEntity(TrablesAdditions.getInstance(), projectile), 1);
        }
    }

    private ItemStack findShooterItem(ProjectileSource source) {
        if (source instanceof LivingEntity livingEntity) {
            EntityEquipment equipment = livingEntity.getEquipment();
            if (equipment == null) return null;
            ItemStack mainHand = equipment.getItemInMainHand();
            if (ShootsProjectiles.getProjectile(mainHand) != null || projectiles.contains(mainHand.getType())) {
//                return ShootsProjectiles.getProjectile(mainHand);
                return mainHand;
            }
            ItemStack offHand = equipment.getItemInOffHand();
            if (ShootsProjectiles.getProjectile(offHand) != null){
                return offHand;
//                return ShootsProjectiles.getProjectile(offHand);
            }
        }
        return null;
    }
}
