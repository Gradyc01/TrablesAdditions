package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

import me.depickcator.trablesAdditions.Game.Effects.GolemLaunch;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Boss.WitherRealmVex;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Boss.WitherRealmWitherSkeleton;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealmBossFight;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.List;
import java.util.Random;

public class WitherRealmWither extends WitherBoss implements RealmNMSMob {
    private final Random random;
    private int BOSS_PHASE;
    private final WitherRealmBossFight bossFight;
    private int mobWaveTicks;
    private int specialAttackTicks;
    private int finalNukeTicks;
    private final int MOB_WAVE_TICKS_REQUIRED = 60 * 20;
    private int SPECIAL_ATTACK_TICKS_REQUIRED = 12 * 20;
    //could just disable mob griefing when he spawns
    public WitherRealmWither(Location location, WitherRealmBossFight bossFight) {
        super(EntityType.WITHER, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        random = new Random();
        this.bossFight = bossFight;
        this.bossFight.setWither(this);
        BOSS_PHASE = 0; //TODO temp
        this.setCustomName(getMobName());
        this.setNoAi(true);
        this.getBukkitEntity().setRotation(270, 0);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(500);
        this.setHealth(500);
    }

    public Location getLocation() {
        return this.getBukkitLivingEntity().getLocation();
    }

    public void startPhaseTwo() {
        this.setNoAi(false);
        BOSS_PHASE = 2;
    }

    public void startPhaseOne() {
        this.setNoAi(true);
        getLocation().getWorld().playSound(getLocation(), Sound.ENTITY_WITHER_SPAWN, 100.0F, 1.0F);
        bossFight.spawnMobWave();
        BOSS_PHASE = 1;
    }

    @Override
    public void aiStep() {
        if (!this.isAlive()) return;
        if (this.isPowered()) {
            finalNukeTick();
        } else if (BOSS_PHASE == 1) {
            mobWaveTick();
        } else if (BOSS_PHASE == 2) {
            specialAttackTick();
        }
        super.aiStep();
    }

    @Override
    public void onRemoval(RemovalReason reason) {
        super.onRemoval(reason);
        bossFight.bossDefeated();
    }


    private void mobWaveTick() {
        if (mobWaveTicks++ >= MOB_WAVE_TICKS_REQUIRED) {
            bossFight.spawnMobWave();
            mobWaveTicks = 0;
        }
    }

    private void specialAttackTick() {
        if (specialAttackTicks++ >= SPECIAL_ATTACK_TICKS_REQUIRED) {
            specialAttackTicks = 0;
            switch (random.nextInt(4)) {
                case 1 -> vexAttack();
                case 2 -> witherSkeletonAttack();
                default -> shootFireballs();
            }
        }
    }

    private void finalNukeTick() {
        if (finalNukeTicks++ >= 20) {
            finalNukeTicks =0;
            ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), (float) 2, false);
            if (event.callEvent()) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), event.getRadius(), false, Level.ExplosionInteraction.MOB);
            }
        }
    }

    private void shootFireballs() {
        AABB boundingBox = this.getBoundingBox().inflate(25.0);
        List<Entity> nearbyEntities = this.level().getEntities(this, boundingBox, e -> e instanceof Player);
        for (Entity nearbyEntity : nearbyEntities) {
            if (nearbyEntity instanceof Player player) {
                fireballAttack(player);
            }
        }
    }

    private void fireballAttack(LivingEntity target) {
        if (target == null) return;
        double d = target.getX() - this.getX();
        double d1 = (target.getY(0.1)) - (this.getY() + 2);
        double d2 = target.getZ() - this.getZ();

        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            Vec3 direction = (new Vec3(d, d1, d2)).normalize().multiply(3.0, 3.4, 3.0);
            WitherRealmFireball fireball = new WitherRealmFireball(level, this, direction, 12);
            fireball.setOwner(this);
            fireball.setPos(this.getX() + direction.x * 1.5, this.getY(0.5) + 0.5, this.getZ() + direction.z * 1.5);
            fireball.setRemainingFireTicks(0);
            fireball.setInvulnerable(true);
            fireball.extinguishFire();
            serverLevel.addFreshEntity(fireball);
        }
    }

    private void vexAttack() {
        for (int i = 0; i<20; i++) {
            new WitherRealmVex(this.getLocation(), random, this);
        }
        getLocation().getWorld().playSound(getLocation(), Sound.ENTITY_VEX_CHARGE, 20.0F, 0.0F);
    }

    private void witherSkeletonAttack() {
        for (int i = 0; i<5; i++) {
            new WitherRealmWitherSkeleton(this.getLocation(), random, this);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        if (BOSS_PHASE == 0) return false;
        float newAmount = this.isPowered() ? amount : amount / 2;
        return super.hurtServer(level, damageSource, newAmount);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity source) {
        return super.doHurtTarget(level, source);
    }

    @Override
    public Component getMobName() {
        return Component.literal("Krivon").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.DARK_PURPLE.value())));
    }

    @Override
    public boolean isPowered() {
//        if (BOSS_PHASE == 0) return true;
        return super.isPowered();
    }


}
