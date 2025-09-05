package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Boss.WitherRealmVex;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Boss.WitherRealmWitherSkeleton;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealmBossFight;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI.WitherRealm_DroppedPanel;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
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
import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class WitherRealmWither extends WitherBoss implements RealmNMSMob {
    private final Random random;
    private int BOSS_PHASE;
    private final WitherRealmBossFight bossFight;
    private int mobWaveTicks;
    private int specialAttackTicks;
    private int finalNukeTicks;
    private int softStunFireball;
    private int stunnedTicks;
    private final float phase2Damage;
    private final int MOB_WAVE_TICKS_REQUIRED = 60 * 20;
    private int SPECIAL_ATTACK_TICKS_REQUIRED = 12 * 20;
    //could just disable mob griefing when he spawns
    public WitherRealmWither(Location location, WitherRealmBossFight bossFight, int phase2Health, int phase3Health) {
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
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(phase3Health * 2);
        this.setHealth(this.getMaxHealth());
        phase2Damage = (float) phase3Health / (float) phase2Health;
//        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(500);
//        this.setHealth(500);
    }

    public WitherRealmWither (Location location, WitherRealmBossFight bossFight) {
        this(location, bossFight, 600, 150);
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
        if (isStunned()) {
            if (stunnedTicks == 1) this.setGlowingTag(false);
            this.stunnedTicks--;
            float yaw = this.getBukkitEntity().getYaw();
            this.getBukkitEntity().setRotation(yaw + 20, 0);
            setBossBarProgress();
            return;
        }
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
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        setBossBarProgress();
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        if (BOSS_PHASE == 0) return false;
        float newAmount = this.isPowered() ? amount : amount * phase2Damage;
        boolean bool = super.hurtServer(level, damageSource, newAmount);
        TextUtil.debugText("Damage Source for Wither Boss", "damageSource: " + damageSource.getDirectEntity().getName());
        if (damageSource.getDirectEntity().getBukkitEntity() instanceof Fireball fireball) {
            if (fireball.getScoreboardTags().contains("soft_stun_fireball")) {
                TextUtil.debugText("Stun", "Stun Fireball");
                softStunWither();
            }
        }
        return bool;
    }

    @Override
    public void heal(float healAmount, EntityRegainHealthEvent.RegainReason regainReason, boolean isFastRegen) {
//        super.heal(healAmount, regainReason, isFastRegen);/
        if (regainReason == EntityRegainHealthEvent.RegainReason.REGEN) return;
        super.heal(healAmount, regainReason, isFastRegen);
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
            if (++softStunFireball >= 3) {
                softStunFireball = 0;
                shootSoftStunFireball();
                return;
            }
            switch (random.nextInt(3)) {
                case 1 -> vexAttack();
                case 2 -> witherSkeletonAttack();
                default -> shootFireballs();
            }
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity source) {
        return super.doHurtTarget(level, source);
    }

    public void fullStunWither() {
        if (!isStunned()) {
            stunnedTicks = 15 * 20;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1, false, Level.ExplosionInteraction.MOB);
            getLocation().getWorld().playSound(getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 2);
            this.setGlowingTag(true);
            setTarget(null);
        }
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

    private void softStunWither() {
        if (!isStunned()) {
            stunnedTicks = 5 * 20;
            new WitherRealm_DroppedPanel(getLocation(), this);
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

    private void shootSoftStunFireball() {
        AABB boundingBox = this.getBoundingBox().inflate(25.0);
        List<Entity> nearbyEntities = this.level().getEntities(this, boundingBox, e -> e instanceof Player);
        if (nearbyEntities.isEmpty()) return;
        Entity entity = nearbyEntities.get(random.nextInt(nearbyEntities.size()));
        Fireball fireball = (Fireball) fireballAttack((LivingEntity) entity, true, 0F, 20).getBukkitEntity();
        fireball.addScoreboardTag("soft_stun_fireball");
        fireball.setIsIncendiary(false);
        fireball.setFireTicks(0);
        fireball.setGlowing(true);
        fireball.setAcceleration(new Vector(0,0 ,0));
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

    private WitherRealmFireball fireballAttack(LivingEntity target) {
        return fireballAttack(target, false, 3, 13);
    }

    private WitherRealmFireball fireballAttack(LivingEntity target, boolean canBeReflected, float speed, int power) {
        if (target == null) return null;
        double d = target.getX() - this.getX();
        double d1 = (target.getY(0.1)) - (this.getY() + 2);
        double d2 = target.getZ() - this.getZ();

        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            Vec3 direction = (new Vec3(d, d1, d2)).normalize().multiply(speed, speed, speed);
            WitherRealmFireball fireball = new WitherRealmFireball(level, this, direction, power, canBeReflected);
            fireball.setOwner(this);
            fireball.setPos(this.getX() + direction.x * 1.5, this.getY(0.5) + 0.5, this.getZ() + direction.z * 1.5);
            fireball.setRemainingFireTicks(0);
            fireball.setInvulnerable(true);
            fireball.extinguishFire();
            serverLevel.addFreshEntity(fireball);
            return fireball;
        }
        return null;
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

    private boolean isStunned() {
        return stunnedTicks > 0;
    }

    private void setBossBarProgress() {
        float phase3Progress = (float) (Float.min(this.getHealth() / (this.getMaxHealth()/2), 1) * phase2Damage);
        float phase2Progress = (float) (Float.max((this.getHealth() - (this.getMaxHealth()/2))/ (this.getMaxHealth()/2), 0) * (1 - phase2Damage));
        bossEvent.setProgress(phase3Progress +phase2Progress);
    }


}
