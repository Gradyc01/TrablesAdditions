package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Effects.GolemLaunch;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ShootsProjectiles;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.CompactTNT;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class IronStaff extends CustomItem implements ItemClick, ShootsProjectiles {
    private static IronStaff instance;
    private IronStaff() {
        super("Iron Staff", "iron_staff");
        registerItem(this, this);
        addProjectile(this, this);
    }

    public static IronStaff getInstance() {
        if (instance == null) instance = new IronStaff();
        return instance;
    }

    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.YELLOW).append(TextUtil.rightClickText()));
        meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
//        meta.setMaxDamage(16);
        List<Component> lore = new ArrayList<>(List.of(
                TextUtil.makeText("Forged in the agony of devotion", TextUtil.DARK_PURPLE),
                TextUtil.makeText("this staff tears the ground", TextUtil.DARK_PURPLE),
                TextUtil.makeText("casting foes skyward before", TextUtil.DARK_PURPLE),
                TextUtil.makeText("slamming them into obedience", TextUtil.DARK_PURPLE)
        ));
        meta.setMaxStackSize(1);
        meta.lore(lore);
        item.setItemMeta(meta);
        addUnrepairable(item);
        addCooldownGroup(item);
        generateUniqueModelString(item);
        return item;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        if (!e.getAction().isRightClick() || e.getHand() == null) return false;
        ItemStack item = e.getItem();
        Player p = pD.getPlayer();
        if (checkCooldown(p, item)) {
            p.playSound(SoundUtil.makeSound(Sound.ENTITY_WIND_CHARGE_THROW, 1, 1), net.kyori.adventure.sound.Sound.Emitter.self());
//            new ProjectileLaunchEvent(createProjectile(p.getEyeLocation(), p)).callEvent();
            EntityShootBowEvent event = new EntityShootBowEvent(p, item, null, createProjectile(p.getEyeLocation(), p),
                    e.getHand(), 1, false);
            event.callEvent();
            return true;
        }
        return false;
    }

    private Projectile createProjectile(Location loc, Player player) {
        Projectile proj = (Projectile) loc.getWorld().spawnEntity(loc, EntityType.SNOWBALL);
        proj.setShooter(player);
        proj.setVelocity(loc.getDirection().multiply(2.0));
        for (Player p : loc.getWorld().getNearbyPlayers(loc, 30)) {
            p.hideEntity(TrablesAdditions.getInstance(), proj);
        }
        return proj;
    }

    /*Returns true if not on cooldown and sets the cooldown, False otherwise*/
    private boolean checkCooldown(Player p, ItemStack item) {
        if (!p.hasCooldown(item) ) {
            p.setCooldown(item, 15 * 20);
            return true;
        }
        return false;
    }

    @Override
    public void applyKey(EntityShootBowEvent event, ItemStack weapon) {
        Projectile e = (Projectile) event.getProjectile();
        World world = e.getWorld();
        Bukkit.getScheduler().runTaskTimer(TrablesAdditions.getInstance(), () -> {
            if (e.isDead()) return;
            world.spawnParticle(Particle.WAX_OFF, e.getLocation(), 1, 0, 0, 0);
        }, 1, 1);
    }

    @Override
    public double setProjectileComponent(EntityDamageByEntityEvent event, LivingEntity victim) {
        return -1;
    }

    @Override
    public boolean onHit(ProjectileHitEvent event) {
        Projectile proj = event.getEntity();
        BoundingBox boundingBox = proj.getBoundingBox().clone();
            boundingBox.expand(7);
            for (Entity entity : proj.getWorld().getNearbyEntities(boundingBox)) {
                if (entity instanceof LivingEntity livingEntity) {
                    if (livingEntity.equals(proj.getShooter())) continue;
                    new GolemLaunch(livingEntity, (LivingEntity) proj.getShooter(), proj.getLocation());
                }
            }
        for (Player player : proj.getWorld().getNearbyPlayers(proj.getLocation(), 5)) {
            if (player.equals(proj.getShooter())) continue;
            new GolemLaunch(player, (LivingEntity) proj.getShooter(), proj.getLocation());
        }
        return false;
    }
}
