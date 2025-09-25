package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ShootsProjectiles;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.*;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmFireball;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class UnreleasedOrdnance extends Craft implements ShootsProjectiles, ItemClick {
    private static UnreleasedOrdnance instance;
    private UnreleasedOrdnance() {
        super("Unreleased Ordnance", "unreleased_ordnance");
        addProjectile(this, this);
        registerClick(this, this);
    }

    protected Recipe initRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, getKey());

        ShapedRecipe recipe = new ShapedRecipe(key, getResult());
        recipe.shape(" BA", "CDA", " BA");
        recipe.setIngredient('A', SpiderSilk.getInstance().getResult());
        recipe.setIngredient('B', CompactTNT.getInstance().getResult());
        recipe.setIngredient('C', KrivonHandle.getInstance().getResult());
        recipe.setIngredient('D', CupidEssence.getInstance().getResult());
        return recipe;
    }

    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.BOW);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.YELLOW));
        meta.addEnchant(Enchantment.FIRE_PROTECTION, 5, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.setMaxDamage(100);
        List<Component> lore = new ArrayList<>(List.of(
                TextUtil.makeText("A cursed weapon powered by destruction", TextUtil.DARK_PURPLE),
                TextUtil.makeText("unleashing a devastating fireball when powered", TextUtil.DARK_PURPLE)
        ));
        meta.lore(lore);
        item.setItemMeta(meta);
        addUnrepairable(item);
        generateUniqueModelString(item);
        return item;
    }

    @Override
    public void applyKey(EntityShootBowEvent event, ItemStack weapon) {
        if (!(event.getEntity() instanceof Player player)) return;
        Damageable meta = (Damageable) weapon.getItemMeta();
        if (event.getForce() > 0.8 && meta.getDamage() == 0) {
            Vector v = player.getEyeLocation().getDirection();
            ServerLevel level = ((CraftWorld) player.getWorld()).getHandle();
            WitherRealmFireball fireball = new WitherRealmFireball(level, player,
                    new Vec3(v.getX(), v.getY(), v.getZ()), 25);
            level.addFreshEntity(fireball);
            meta.setDamage(meta.getMaxDamage() - 1);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
            weapon.setItemMeta(meta);
        }
        event.setCancelled(true);
    }

    @Override
    public double setProjectileComponent(EntityDamageByEntityEvent event, LivingEntity victim) {
        return -1;
    }

    @Override
    public boolean onHit(ProjectileHitEvent event) {
        return true;
    }

    public static UnreleasedOrdnance getInstance() {
        if (instance == null) instance = new UnreleasedOrdnance();
        return instance;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
//        return false;
        ItemStack item = e.getItem();
        if (item == null) return false;
        Damageable meta = (Damageable) item.getItemMeta();
        return meta.getDamage() == 0;
    }
}
