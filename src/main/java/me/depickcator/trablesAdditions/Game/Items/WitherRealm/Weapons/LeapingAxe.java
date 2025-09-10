package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemCooldown;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Weapon;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.SpiderSilk;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;

public class LeapingAxe extends Weapon implements ItemClick, ItemCooldown {
    private static LeapingAxe instance;
    private LeapingAxe() {
        super("Leaping Axe", "leaping_axe", 7, -2.4);
        registerItem(this, this);
    }

    protected Recipe initRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, getKey());

        ShapedRecipe recipe = new ShapedRecipe(key, getResult());
        recipe.shape("AAA", "ABA", "AAA");
        recipe.setIngredient('A', SpiderSilk.getInstance().getResult());
        recipe.setIngredient('B', Material.GOLDEN_AXE);
        return recipe;
    }

    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.GOLDEN_AXE);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.YELLOW).append(TextUtil.rightClickText()));
        meta.setMaxDamage(1561);
        NamespacedKey key = new NamespacedKey(plugin, getKey());
        AttributeModifier modifier = new AttributeModifier(key, -0.75, AttributeModifier.Operation.ADD_SCALAR);
        meta.addAttributeModifier(Attribute.FALL_DAMAGE_MULTIPLIER, modifier);
        item.setItemMeta(meta);
//        addModifiers(item);
        addCooldownGroup(item);
        generateUniqueModelString(item);
        return item;
    }

    public static LeapingAxe getInstance() {
        if (instance == null) instance = new LeapingAxe();
        return instance;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        if (!e.getAction().isRightClick()) return true;
        ItemStack item = e.getItem();
        Player p = pD.getPlayer();
        if (checkCooldown(p, item)) {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SPIDER_AMBIENT, 2.0F, 1.0F);
            p.setVelocity(p.getEyeLocation().getDirection().multiply(2.0));
            launchAnimation(p.getLocation());
        }
        return false;
    }

    private void launchAnimation(Location loc) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;
                Vector v = new Vector(x, 0.5, z);
                FallingBlock e = (FallingBlock) loc.getWorld().spawnEntity(loc, EntityType.FALLING_BLOCK);
                e.setBlockData(Material.COBWEB.createBlockData());
                e.setCancelDrop(true);
                e.setVelocity(v);
            }
        }

    }

    @Override
    public boolean onCustomWeaponUse(EntityDamageByEntityEvent event, LivingEntity attacker, LivingEntity target) {
        return true;
    }

    @Override
    public boolean checkCooldown(Player player, ItemStack item) {
        return ItemUtil.checkCooldown(player, item, 7);
    }
}
