package me.depickcator.trablesAdditions.Listeners;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.ShootsProjectiles;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.world.effect.MobEffect;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class onDamage extends TrablesListeners {
    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent event) {
        if (isProjectileAttack(event)) {
            AbstractArrow arrow = (AbstractArrow) event.getDamager();
            setSpecialArrowIfNecessary(event, arrow.getWeapon());
        }
        if (event.getEntity() instanceof Player player) {
            playerDamagedEffects(event, player);
        }
        if (event.getDamager() instanceof Explosive explosive) dealWithExplosiveDamage(explosive, event);
    }

    private void dealWithExplosiveDamage(Explosive explosive, EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();
        Entity owner = explosive instanceof TNTPrimed tnt ? tnt.getSource() :
                explosive instanceof Projectile projectile ? (Entity) projectile.getShooter() : null;

        if (!(victim instanceof Player) && owner != null && !(owner instanceof Player)) {
            TextUtil.debugText("Explosive", "reducing damage for" + victim.getName());
            event.setDamage(event.getDamage()/10);
        }
    }

    private void playerDamagedEffects(EntityDamageByEntityEvent event, Player victim) {
        if (isProjectileAttack(event) && ((AbstractArrow) event.getDamager()).getShooter() instanceof Player attacker) {
            sendArrowDamageMessage(victim, attacker, event);
        }
    }

    private void setSpecialArrowIfNecessary(EntityDamageByEntityEvent event, ItemStack weapon) {
        try {
            ShootsProjectiles customWeapon = ShootsProjectiles.getProjectile(weapon);
            if (customWeapon != null && (!(event.getEntity() instanceof Player) || event.getFinalDamage() != 0)) {
                double newDamage = customWeapon.setProjectileComponent(event, (LivingEntity) event.getEntity());
                if (newDamage != -1) event.setDamage(newDamage);
            }
        } catch (Exception ignored) {}
    }



    private boolean isMeleeAttack(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof LivingEntity;
    }

    private boolean isProjectileAttack(EntityDamageByEntityEvent event) {
//        if (event.getDamager() instanceof AbstractArrow) {
//            return ((AbstractArrow) event.getDamager()).getShooter() instanceof Player;
//        }
        return event.getDamager() instanceof AbstractArrow;
//        return false;
    }

    private void sendArrowDamageMessage(Player victim, Player damager, EntityDamageByEntityEvent event) {
        double health = victim.getHealth() - event.getFinalDamage();
        if (health <= 0) return;
        Component name = TextUtil.makeText(victim.getName(), TextUtil.RED);
        Component isAt = TextUtil.makeText(" is at ", TextUtil.YELLOW);
        Component num = TextUtil.makeText(  (double) ((int) (health * 10))/10 + "", TextUtil.RED);
        Component hp = TextUtil.makeText(" HP!", TextUtil.YELLOW);
        damager.sendMessage(name.append(isAt).append(num).append(hp));
    }
}
