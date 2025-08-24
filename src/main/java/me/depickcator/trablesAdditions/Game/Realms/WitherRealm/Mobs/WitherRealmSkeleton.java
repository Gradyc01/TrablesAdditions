package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
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
        generateRandomArmor();
    }

    private void giveAttributes() {
        this.equipment.set(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(initBow()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.getAttribute(Attributes.ARMOR).setBaseValue(5.0F);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(25.0F);
        this.setHealth(this.getMaxHealth());
        this.setShouldBurnInDay(false);
        this.setPersistenceRequired(true);
        super.targetSelector.removeAllGoals(goal -> true);
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Skeleton.class)));
        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
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

    private void generateRandomArmor() {
        EntityEquipment equipment = this.equipment;
        equipment.set(EquipmentSlot.HEAD,
                CraftItemStack.asNMSCopy(
                        CraftItemStack.asNewCraftStack(
                                Mob.getEquipmentForSlot(EquipmentSlot.HEAD, generateNumber()))));
        equipment.set(EquipmentSlot.CHEST,
                CraftItemStack.asNMSCopy(
                        CraftItemStack.asNewCraftStack(
                                Mob.getEquipmentForSlot(EquipmentSlot.CHEST, generateNumber()))));
        equipment.set(EquipmentSlot.LEGS,
                CraftItemStack.asNMSCopy(
                        CraftItemStack.asNewCraftStack(
                                Mob.getEquipmentForSlot(EquipmentSlot.LEGS, generateNumber()))));
        equipment.set(EquipmentSlot.FEET,
                CraftItemStack.asNMSCopy(
                        CraftItemStack.asNewCraftStack(
                                Mob.getEquipmentForSlot(EquipmentSlot.FEET, generateNumber()))));
    }

    private int generateNumber() {
        if (random.nextDouble() < 0.75) {
            return -1;
        } else if (random.nextDouble() < 0.85) {
            return 0;
        } else if (random.nextDouble() < 0.93) {
            return 1;
        } else if (random.nextDouble() < 0.98) {
            return 2;
        }
        return 3;
    }
}
