package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Minibosses;

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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class WitherRealmMiniGolem extends IronGolem implements RealmNMSMob {
    private int timer;
    private final Component name;
    private final int timeTillDetonation;

    public WitherRealmMiniGolem(Location location, Random r) {
        super(EntityType.IRON_GOLEM, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY() + 2, location.getZ());
//        this.setDeltaMovement(new Vec3(r.nextFloat(-2f, 2f), r.nextFloat(0.5f, 2), r.nextFloat(-2f, 2f)));
//        this.hasImpulse = true;
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
//        this.getBukkitLivingEntity().setVelocity(this.getBukkitLivingEntity().getVelocity().multiply(new Vector(20, r.nextFloat(0.5f, 2), 5)));
        name = getMobName();
        timeTillDetonation = 120;
        super.goalSelector.removeAllGoals(goal -> true);
        super.goalSelector.addGoal(1, new MeleeAttackGoal(this, (double)1.0F, true));
        super.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.5, 32.0F));

        super.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        super.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        super.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        super.targetSelector.removeAllGoals(goal -> true);
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Skeleton.class, Stray.class, Bogged.class)));
//        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.getAttribute(Attributes.ARMOR).setBaseValue(10);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getAttribute(Attributes.SAFE_FALL_DISTANCE).setBaseValue(20.0);
        this.getAttribute(Attributes.SCALE).setBaseValue(0.7);

        IronGolem golem = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                golem.getBukkitLivingEntity().setVelocity(
                        new Vector(r.nextFloat(-2f, 2f), r.nextFloat(0.5f, 2), r.nextFloat(-2f, 2f)));
            }
        }.runTaskLater(TrablesAdditions.getInstance(), 2);

    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            if (timer == timeTillDetonation) {
                detonate();
            }
            if (timer++ >= 250) timer = 0;
        }

    }

    private void detonate() {
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), (float) 4, false);
        if (event.callEvent()) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), event.getRadius(), event.getFire(),
                    Level.ExplosionInteraction.MOB);
        }
        this.remove(RemovalReason.DISCARDED);
    }



    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        boolean bool = super.hurtServer(level, damageSource, amount);
        this.setCustomName(NMSMobUtil.generateHealthText(name, this));
        return bool;
    }

    @Override
    public Component getMobName() {
        return Component.literal("Minion").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.WHITE.value())));
    }
}
