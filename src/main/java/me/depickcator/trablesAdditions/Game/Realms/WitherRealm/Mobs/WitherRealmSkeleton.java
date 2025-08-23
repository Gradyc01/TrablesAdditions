package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WitherRealmSkeleton extends Skeleton {
    private final List<ItemStack> loot;
    private final Random random;
    public WitherRealmSkeleton(Location location, Random random) {
        super(EntityType.SKELETON, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        this.random = random;
        giveAttributes();
        loot = new ArrayList<>(List.of(new ItemStack(Material.BONE)));
    }

    private void giveAttributes() {
        this.equipment.set(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(initBow()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.getAttribute(Attributes.ARMOR).setBaseValue(5.0F);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(25.0F);
        this.setHealth(this.getMaxHealth());
        this.setShouldBurnInDay(false);
        this.setPersistenceRequired(true);
        this.reassessWeaponGoal();
//        this.equipment(EquipmentSlot.Mob.getEquipmentForSlot(EquipmentSlot.HEAD, random.nextInt(4));
    }



    private ItemStack initBow() {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();
        meta.addEnchant(Enchantment.POWER, 4, true);
        if (random.nextDouble() < 0.05) meta.addEnchant(Enchantment.FLAME, 1, true);
        if (random.nextDouble() < 0.10) meta.addEnchant(Enchantment.PUNCH, 2, true);
        bow.setItemMeta(meta);
        return bow;
    }
}
