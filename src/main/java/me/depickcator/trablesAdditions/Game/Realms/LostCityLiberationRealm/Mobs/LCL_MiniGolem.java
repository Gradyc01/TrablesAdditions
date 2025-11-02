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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.SplashPotion;
import org.jetbrains.annotations.NotNull;

public class LCL_MiniGolem extends IronGolem implements RealmNMSMob {
    public LCL_MiniGolem(Location location) {
        super(EntityType.IRON_GOLEM, ((CraftWorld) location.getWorld()).getHandle());
        NMSMobUtil.setAndSpawn(this, location);
        goalSelector.removeAllGoals(goal -> true);
        goalSelector.addGoal(1, new MeleeAttackGoal(this, (double)1.0F, true));
        goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.2, 40.0F));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 15.0F));
        targetSelector.removeAllGoals(goal -> true);
        targetSelector.addGoal(1, (new HurtByTargetGoal(this,
                Mob.class, Pillager.class, ZombieVillager.class, Witch.class, Vindicator.class, SplashPotionItem.class)));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.getAttribute(Attributes.ARMOR).setBaseValue(10);
//        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4);
        this.getAttribute(Attributes.WATER_MOVEMENT_EFFICIENCY).setBaseValue(1.0);
        this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(2.0);
        this.getAttribute(Attributes.SCALE).setBaseValue(0.80);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(32);
        this.setPersistenceRequired(true);
    }

    @Override
    public boolean canAttackType(@NotNull EntityType<?> type) {
        return type == EntityType.PLAYER;
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
        return Component.literal("Iron Youth").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.AQUA.value())));
    }
}
