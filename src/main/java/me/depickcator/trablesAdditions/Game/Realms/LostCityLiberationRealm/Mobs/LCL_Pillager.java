package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class LCL_Pillager extends Pillager implements RealmNMSMob {
    public LCL_Pillager(Location location, Random random) {
        super(EntityType.PILLAGER, ((CraftWorld) location.getWorld()).getHandle());
        NMSMobUtil.setAndSpawn(this, location);
        this.targetSelector.removeAllGoals(goal -> true);
        this.goalSelector.removeAllGoals(goal -> true);
        super.goalSelector.addGoal(0, new FloatGoal(this));
//        super.goalSelector.addGoal(2, new Raider.HoldGroundAttackGoal(this, 10.0F));
        super.goalSelector.addGoal(3, new RangedCrossbowAttackGoal<>(this, 1.0F, 12.0F));
        super.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        super.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Mob.class)));
        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
//        double speed = this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
//        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed + (random.nextFloat() - 0.4) * (speed/4));
        this.getAttribute(Attributes.ARMOR).setBaseValue(5.0F);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0F);
        this.setPersistenceRequired(true);
        this.equipment.set(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(getWeapon(random)));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    private ItemStack getWeapon(Random random) {
        ItemStack bow = new ItemStack(Material.CROSSBOW);
        ItemMeta meta = bow.getItemMeta();
        meta.addEnchant(Enchantment.POWER, 6, true);
        if (random.nextDouble() < 0.05) meta.addEnchant(Enchantment.FLAME, 1, true);
        if (random.nextDouble() < 0.10) meta.addEnchant(Enchantment.PUNCH, 4, true);
        if (random.nextDouble() < 0.05) meta.addEnchant(Enchantment.MULTISHOT, 1, true);
        meta.setEnchantmentGlintOverride(false);
        bow.setItemMeta(meta);
        ItemMeta meta2 = bow.getItemMeta();
        if (meta2.getEnchants().size() > 1) meta2.setEnchantmentGlintOverride(true);
        bow.setItemMeta(meta2);
        return bow;
    }

    @Override
    protected void dropFromLootTable(ServerLevel level, DamageSource damageSource, boolean playerKill) {
//        NMSMobUtil.attemptToDropItemStack(ZombieHeart.getInstance().getResult(),  damageSource,this, 0.005);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        boolean bool = super.hurtServer(level, damageSource, amount);
        this.setCustomName(NMSMobUtil.generateHealthText(getMobName(), this));
        return bool;
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel level, @NotNull Entity source) {
        return super.doHurtTarget(level, source);
    }

    @Override
    public Component getMobName() {
        return Component.literal("Lost City Archer").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.WHITE.value())));
    }
}
