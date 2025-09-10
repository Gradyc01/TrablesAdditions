package me.depickcator.trablesAdditions.Listeners;

import io.papermc.paper.tag.EntityTags;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ShootsProjectiles;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Weapon;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class EntityDamage extends TrablesListeners {
    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (isMeleeAttack(event)) {
            dealWithMeleeAttacks(event);
        }
//        EntityTags.UNDEADS.isTagged(event.getEntityType());
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
            TextUtil.debugText("Special Arrow", "Attempting to find custom weapon... ");
            ShootsProjectiles customWeapon = ShootsProjectiles.getProjectile(weapon);
            if (customWeapon != null && (!(event.getEntity() instanceof Player) || event.getFinalDamage() != 0) &&
                    event.getEntity() instanceof LivingEntity livingEntity) {
                TextUtil.debugText("Special Arrow", "Weapon found!");
                double newDamage = customWeapon.setProjectileComponent(event, livingEntity);
                if (newDamage != -1) event.setDamage(newDamage);
            }
        } catch (Exception ignored) {}
    }

    private void dealWithMeleeAttacks(EntityDamageByEntityEvent event) {
        LivingEntity attacker =  (LivingEntity) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();
        EntityEquipment equipment = attacker.getEquipment();
        if (equipment == null) return;
        Weapon weapon = Weapon.findCustomWeapon(equipment.getItemInMainHand());
        if (weapon != null) event.setCancelled(!weapon.onCustomWeaponUse(event, attacker, victim));
    }

    private boolean isMeleeAttack(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof LivingEntity;
    }

    private boolean isProjectileAttack(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof AbstractArrow;
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
