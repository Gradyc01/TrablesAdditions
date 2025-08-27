package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.checkerframework.checker.units.qual.A;

public class WitherRealmZombieKnight extends Zombie implements RealmNMSMob {
    private final Component name;
    public WitherRealmZombieKnight(Location location) {
        super(EntityType.ZOMBIE, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        name = getMobName();
        giveAttributes();
    }

    private void giveAttributes() {
        this.equipment.set(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(initSword()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(52.0F);
        this.equipment.set(EquipmentSlot.HEAD, initArmor(Material.LEATHER_HELMET, "Helmet", 3.5));
        this.equipment.set(EquipmentSlot.CHEST, initArmor(Material.LEATHER_CHESTPLATE, "Chestplate", 8));
        this.equipment.set(EquipmentSlot.LEGS, initArmor(Material.LEATHER_LEGGINGS, "Leggings", 6));
        this.equipment.set(EquipmentSlot.FEET, initArmor(Material.LEATHER_BOOTS, "Boots", 3.5));
        super.targetSelector.removeAllGoals(goal -> true);
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Skeleton.class, Stray.class)));
        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.setHealth(this.getMaxHealth());
        this.setShouldBurnInDay(false);
        this.setPersistenceRequired(true);
//        this.equipment(EquipmentSlot.Mob.getEquipmentForSlot(EquipmentSlot.HEAD, random.nextInt(4));
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        boolean bool = super.hurtServer(level, damageSource, amount);
        this.setCustomName(NMSMobUtil.generateHealthText(name, this));
        return bool;
    }


    private ItemStack initSword() {
        ItemStack item = new ItemStack(Material.DIAMOND_HOE);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.customName(TextUtil.makeText("zombie Knight's Sword", TextUtil.YELLOW));
        meta.addEnchant(Enchantment.SHARPNESS, 6, true);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(
                new NamespacedKey(TrablesAdditions.getInstance(), "zombie_knight_sword")
                , 1, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.MOVEMENT_SPEED, new AttributeModifier(
                new NamespacedKey(TrablesAdditions.getInstance(), "zombie_knight_sword_speed")
                , 0.1, AttributeModifier.Operation.ADD_NUMBER));
        meta.setMaxDamage(255);
        item.setItemMeta(meta);
        return item;
    }

    private net.minecraft.world.item.ItemStack initArmor(Material material, String name, double amount) {
        ItemStack item = new ItemStack(material);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.customName(TextUtil.makeText("Zombie Knight " + name, TextUtil.YELLOW));
        meta.addAttributeModifier(Attribute.ARMOR, new AttributeModifier(
                new NamespacedKey(TrablesAdditions.getInstance(), "zombie_knight_" + name),
                amount, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.MAX_HEALTH, new AttributeModifier(
                new NamespacedKey(TrablesAdditions.getInstance(), "zombie_knight_" + name + "_health"),
                2, AttributeModifier.Operation.ADD_NUMBER));
        meta.setMaxDamage(600);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        LeatherArmorMeta meta1 = (LeatherArmorMeta) item.getItemMeta();
        meta1.setColor(Color.fromRGB(255, 105, 180));
        item.setItemMeta(meta1);
        return CraftItemStack.asNMSCopy(item);
    }


    @Override
    public Component getMobName() {
        return Component.literal("Zombie Knight").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.YELLOW.value())));
    }
}
