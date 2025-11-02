package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Effects.NatureWrath;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ShootsProjectiles;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.KrivonHandle;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.OceanCore;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ThunderCore;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ExplosionResult;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Torpedo extends Craft implements ShootsProjectiles {
    private static Torpedo instance;
    private Torpedo() {
        super("Torpedo", "torpedo");
        addProjectile(this, this);
    }

    protected Recipe initRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, getKey());

        ShapedRecipe recipe = new ShapedRecipe(key, getResult());
        recipe.shape("AAA", "AAA", "AAA");
        recipe.setIngredient('A', Material.BARRIER);
        return recipe;
    }

    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.TRIDENT);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.YELLOW));
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
        event.setCancelled(true);
        if (event.getEntity() instanceof Player player) {
            player.setVelocity(player.getEyeLocation().getDirection().multiply(5));
            player.startRiptideAttack(20, 10, event.getBow());
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_2, 1.0f, 1.0f);
            explode(player);

            player.setCooldown(weapon, 60 * 20);
            Damageable meta = (Damageable) weapon.getItemMeta();
            meta.setDamage(meta.getDamage() + 1);
            weapon.setItemMeta(meta);
        }
    }

    private void explode(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                TextUtil.debugText(player.getName(), "Riptide Explosion");
                if (player.isRiptiding()) {
                    Event e = new EntityExplodeEvent(player, player.getLocation(), List.of(), 5, ExplosionResult.DESTROY);
                    if (e.callEvent()) player.getWorld().createExplosion(player, player.getLocation(),
                                6, false, true, true);
                } else cancel();
            }
        }.runTaskTimer(plugin, 3, 1);
    }

    @Override
    public double setProjectileComponent(EntityDamageByEntityEvent event, LivingEntity victim) {
        return -1;
    }

    @Override
    public boolean onHit(ProjectileHitEvent event) {
        return true;
    }

    public static Torpedo getInstance() {
        if (instance == null) instance = new Torpedo();
        return instance;
    }
}
