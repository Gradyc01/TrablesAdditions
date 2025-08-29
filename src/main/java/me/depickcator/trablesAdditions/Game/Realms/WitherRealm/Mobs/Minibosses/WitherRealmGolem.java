package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Minibosses;

import me.depickcator.trablesAdditions.Game.Effects.GolemLaunch;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

import java.util.List;
import java.util.Random;

public abstract class WitherRealmGolem extends IronGolem implements RealmNMSMob {
    protected final Random random;
    private final Component name;
    private double healthThreshold;

    public WitherRealmGolem(Location location, Random random) {
        super(EntityType.IRON_GOLEM, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        this.random = random;
        name = getMobName();
        healthThreshold = random.nextDouble(0.75, 0.9);
        super.goalSelector.removeAllGoals(goal -> true);
        super.goalSelector.addGoal(1, new MeleeAttackGoal(this, (double)1.0F, true));
        super.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.5, 40.0F));
        super.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        super.targetSelector.removeAllGoals(goal -> true);
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Skeleton.class, Stray.class, Bogged.class)));
        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.getAttribute(Attributes.ARMOR).setBaseValue(30);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
        this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(2.0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        boolean bool = super.hurtServer(level, damageSource, amount);
        this.setCustomName(NMSMobUtil.generateHealthText(name, this));
        checkHealth();
        return bool;
    }

    private void checkHealth() {
        if (this.getHealth()/this.getMaxHealth() < healthThreshold && healthThreshold > 0) {
            healthThreshold -= random.nextDouble(0.20, 0.30);
            launchAttack();
        }
    }

    private void launchAttack() {
        AABB boundingBox = this.getBoundingBox().inflate(12.0);
        List<Entity> nearbyEntities = this.level().getEntities(this, boundingBox, e -> e instanceof Player);
        for (Entity nearbyEntity : nearbyEntities) {
            if (nearbyEntity instanceof Player player) {
                TextUtil.debugText("Iron Golem", player.getName().getString());
                new GolemLaunch(player.getBukkitLivingEntity(), this.getBukkitLivingEntity());
            }
        }
    }
}
