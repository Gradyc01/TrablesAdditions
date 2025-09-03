package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Zombies;

import me.depickcator.trablesAdditions.Game.Items.Uncraftable.WitherRealmDiscipleTrident;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WitherRealmDrowned extends Drowned implements RealmNMSMob {
    private final List<ItemStack> loot;
    private Component name;

    public WitherRealmDrowned(Location location) {
        super(EntityType.DROWNED, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        name = getMobName();
        giveAttributes();
        super.navigation = new GroundPathNavigation(this, ((CraftWorld) location.getWorld()).getHandle());
        super.moveControl = new MoveControl(this);
        loot = new ArrayList<>(List.of(new ItemStack(Material.ROTTEN_FLESH)));
        super.goalSelector.removeAllGoals(goal -> true);
        super.goalSelector.addGoal(2, new DrownedTridentAttackGoal(this, 1.0, 40, 10));
        super.goalSelector.addGoal(2, new DrownedAttackGoal(this, (double)1.0F, false));
        super.targetSelector.removeAllGoals(goal -> true);
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Skeleton.class, Stray.class, Bogged.class, Drowned.class)));
        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.equipment.set(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(WitherRealmDiscipleTrident.getInstance().getResult()));
        this.equipment.set(EquipmentSlot.HEAD, initArmor(Material.LEATHER_HELMET, "Helmet", 2.5));
        this.equipment.set(EquipmentSlot.CHEST, initArmor(Material.LEATHER_CHESTPLATE, "Chestplate", 2.5));
        this.equipment.set(EquipmentSlot.LEGS, initArmor(Material.LEATHER_LEGGINGS, "Leggings", 2.5));
        this.equipment.set(EquipmentSlot.FEET, initArmor(Material.LEATHER_BOOTS, "Boots", 2.5));

    }

    @Override
    protected void dropFromLootTable(ServerLevel level, DamageSource damageSource, boolean playerKill) {
        if (playerKill) {
            for (ItemStack stack : loot) {
                this.spawnAtLocation(level, CraftItemStack.asNMSCopy(stack));
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        boolean bool = super.hurtServer(level, damageSource, amount);
        this.setCustomName(NMSMobUtil.generateHealthText(name, this));
        return bool;
    }

    private void giveAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.15F);
//        this.getAttribute(Attributes.ARMOR).setBaseValue(10.0F);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(75.0F);
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(35.0F);
        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0F);
        this.setShouldBurnInDay(false);
        this.setPersistenceRequired(true);
    }

    private net.minecraft.world.item.ItemStack initArmor(Material material, String name, double amount) {
        ItemStack item = new ItemStack(material);
        Damageable meta = (Damageable) item.getItemMeta();
        meta.customName(TextUtil.makeText("Heavy Disciple " + name, TextUtil.YELLOW));
        meta.addAttributeModifier(Attribute.ARMOR, new AttributeModifier(
                new NamespacedKey(TrablesAdditions.getInstance(), "heavy_disciple_" + name),
                amount, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.MOVEMENT_SPEED, new AttributeModifier(
                new NamespacedKey(TrablesAdditions.getInstance(), "heavy_disciple_" + name + "_health"),
                -0.025, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.KNOCKBACK_RESISTANCE,
                new AttributeModifier(new NamespacedKey(TrablesAdditions.getInstance(), "heavy_disciple_knockback_" + name),
                        0.05, AttributeModifier.Operation.ADD_NUMBER));
        meta.setMaxDamage(1000);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        LeatherArmorMeta meta1 = (LeatherArmorMeta) item.getItemMeta();
        meta1.setColor(Color.fromRGB(14,102,109));
        item.setItemMeta(meta1);
        return CraftItemStack.asNMSCopy(item);
    }

    @Override
    public boolean okTarget(@Nullable LivingEntity target) {
        return target != null;
    }

    @Override
    public Component getMobName() {
        return Component.literal("Krivon's Disciple").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.GOLD.value())));
    }

    static class DrownedAttackGoal extends ZombieAttackGoal {
        private final Drowned drowned;

        public DrownedAttackGoal(Drowned drowned, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(drowned, speedModifier, followingTargetEvenIfNotSeen);
            this.drowned = drowned;
        }

        public boolean canUse() {
            return super.canUse() && this.drowned.okTarget(this.drowned.getTarget());
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.drowned.okTarget(this.drowned.getTarget());
        }
    }

    static class DrownedTridentAttackGoal extends RangedAttackGoal {
        private final Drowned drowned;

        public DrownedTridentAttackGoal(RangedAttackMob rangedAttackMob, double speedModifier, int attackInterval, float attackRadius) {
            super(rangedAttackMob, speedModifier, attackInterval, attackRadius);
            this.drowned = (Drowned)rangedAttackMob;
        }

        public boolean canUse() {
            return super.canUse() && this.drowned.getMainHandItem().is(Items.TRIDENT);
        }

        public void start() {
            super.start();
            this.drowned.setAggressive(true);
            this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
        }

        public void stop() {
            super.stop();
            this.drowned.stopUsingItem();
            this.drowned.setAggressive(false);
        }
    }


}
