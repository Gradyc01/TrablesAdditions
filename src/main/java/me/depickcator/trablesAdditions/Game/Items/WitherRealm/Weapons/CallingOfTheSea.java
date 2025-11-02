package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Effects.GolemLaunch;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ShootsProjectiles;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.OceanCore;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ShatteredQuiver;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.SpiderSilk;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallingOfTheSea extends Craft implements ItemClick {
    private static CallingOfTheSea instance;
    private CallingOfTheSea() {
        super("Calling of the Sea", "calling_of_the_sea");
        registerClick(this, this);
    }

    public static CallingOfTheSea getInstance() {
        if (instance == null) instance = new CallingOfTheSea();
        return instance;
    }

    @Override
    protected Recipe initRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, getKey());
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("  B", " AC", "A C");
        recipe.setIngredient('A', OceanCore.getInstance().getResult());
        recipe.setIngredient('B', Material.WATER_BUCKET);
        recipe.setIngredient('C', SpiderSilk.getInstance().getResult());
        return recipe;
    }

    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.AQUA).append(TextUtil.rightClickText()));
        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 3, true);
        List<Component> lore = new ArrayList<>(List.of(
                TextUtil.makeText("Embrace the powers of the ", TextUtil.DARK_PURPLE),
                TextUtil.makeText("ocean and summon it's might", TextUtil.DARK_PURPLE),
                TextUtil.makeText("forming a unyielding wall of", TextUtil.DARK_PURPLE),
                TextUtil.makeText("water against your enemies", TextUtil.DARK_PURPLE)
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
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 5, 0);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 5, 1);
            spawnWater(createProjectile(p.getEyeLocation(), p), p);
            return true;
        }
        return false;
    }

    private void spawnWater(Projectile projectile, Player p) {
        Map<Block, Instant> blocks = new HashMap<>();
        int ticks = 5 * 20;
        new BukkitRunnable() {
            int time = 0;
            @Override
            public void run() {
                Location loc = projectile.getLocation();
                if (time++ < ticks && !projectile.isDead() && loc.getBlock().getType().isAir() ) {
                    Block b = loc.getBlock();
                    blocks.put(b, Instant.now());
                    PlayerBucketEmptyEvent event = new PlayerBucketEmptyEvent(p, b, b, b.getFace(b),
                            Material.WATER_BUCKET, new ItemStack(Material.WATER_BUCKET), EquipmentSlot.HAND);
                    if (!event.callEvent()) {
                        cancel();
                        return;
                    }
                    loc.getBlock().setType(Material.WATER);
                }
                if (blocks.isEmpty()) cancel();
                for (Map.Entry<Block, Instant> entry : new HashMap<>(blocks).entrySet()) {
                    if (Instant.now().isAfter(entry.getValue().plusSeconds(ticks/20))) {
                        entry.getKey().setType(Material.AIR);
                        blocks.remove(entry.getKey());
                    }
                }
            }
        }.runTaskTimer(plugin, 5, 1);
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
            p.setCooldown(item, 90 * 20);
            return true;
        }
        return false;
    }

}
