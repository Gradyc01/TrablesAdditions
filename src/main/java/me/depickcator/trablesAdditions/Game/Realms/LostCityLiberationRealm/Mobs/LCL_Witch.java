package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs;

import com.destroystokyo.paper.event.entity.WitchThrowPotionEvent;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.jetbrains.annotations.NotNull;

public class LCL_Witch extends Witch implements RealmNMSMob {
    public LCL_Witch(Location location) {
        super(EntityType.WITCH, ((CraftWorld) location.getWorld()).getHandle());
        NMSMobUtil.setAndSpawn(this, location);
        this.getAttribute(Attributes.ARMOR).setBaseValue(20.0F);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50.0F);
        this.setHealth(this.getMaxHealth());
        this.goalSelector.removeAllGoals(goal -> true);
        this.targetSelector.removeAllGoals(goal -> true);
        super.goalSelector.addGoal(1, new FloatGoal(this));
        super.goalSelector.addGoal(2, new RangedAttackGoal(this, (double)1.0F, 10, 20.0F));
        super.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 20.0F));
        super.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        super.targetSelector.addGoal(1, new HurtByTargetGoal(this, Mob.class));
        super.targetSelector.addGoal(2, new NearestHealableRaiderTargetGoal<>
                (this, Mob.class, true, (entity, level) -> true));
        super.targetSelector.addGoal(3, new NearestAttackableWitchTargetGoal<>
                (this, Player.class, 10, true, false, null));
        this.setPersistenceRequired(true);
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
        if (source instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 10 * 20, 2), this, EntityPotionEffectEvent.Cause.ATTACK);
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20, 2), this, EntityPotionEffectEvent.Cause.ATTACK);
        } else if (source instanceof LivingEntity entity) {
            entity.setHealth(entity.getMaxHealth());
        }
        return super.doHurtTarget(level, source);
        //Doesn't Work
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        Vec3 deltaMovement = target.getDeltaMovement();
        double d = target.getX() + deltaMovement.x - this.getX();
        double d1 = target.getEyeY() - (double)1.1F - this.getY();
        double d2 = target.getZ() + deltaMovement.z - this.getZ();
        double squareRoot = Math.sqrt(d * d + d2 * d2);
        Holder<Potion> holder = Potions.HARMING;
        if (target instanceof Raider) {
            if (target.getHealth() <= 10.0F) {
                holder = Potions.HEALING;
            } else {
                holder = Potions.REGENERATION;
            }

            this.setTarget((LivingEntity)null);
        } else if (squareRoot >= (double)8.0F && !target.hasEffect(MobEffects.SLOWNESS)) {
            holder = Potions.SLOWNESS;
        } else if (target.getHealth() >= 8.0F && !target.hasEffect(MobEffects.POISON)) {
            holder = Potions.POISON;
        } else if (squareRoot <= (double)3.0F && !target.hasEffect(MobEffects.WEAKNESS) && super.random.nextFloat() < 0.25F) {
            holder = Potions.WEAKNESS;
        }

        Level var14 = this.level();
        if (var14 instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)var14;
            ItemStack itemStack = PotionContents.createItemStack(Items.LINGERING_POTION, holder);
            WitchThrowPotionEvent event = new WitchThrowPotionEvent((org.bukkit.entity.Witch)this.getBukkitEntity(), (org.bukkit.entity.LivingEntity)target.getBukkitEntity(), CraftItemStack.asCraftMirror(itemStack));
            if (!event.callEvent()) {
                return;
            }
            itemStack = CraftItemStack.asNMSCopy(event.getPotion());
            Projectile.spawnProjectileUsingShoot(ThrownSplashPotion::new, serverLevel, itemStack, this, d, d1 + squareRoot * 0.2, d2, 0.75F, 8.0F);
        }
    }

    @Override
    public Component getMobName() {
        return Component.literal("Lost Sister").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.WHITE.value())));
    }
}
