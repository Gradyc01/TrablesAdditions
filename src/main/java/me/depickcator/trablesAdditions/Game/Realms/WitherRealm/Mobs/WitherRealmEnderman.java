package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

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
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

import java.lang.reflect.Field;
import java.util.Random;

public class WitherRealmEnderman extends EnderMan implements RealmNMSMob {

    public WitherRealmEnderman(Location location, Random random) {
        super(EntityType.ENDERMAN, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
        this.setYRot(this.getYRot());
        this.setYHeadRot(this.getYHeadRot() + 180);
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(12.0F);
        super.goalSelector.removeAllGoals(goal -> true);
        super.goalSelector.addGoal(0, new FloatGoal(this));
        super.goalSelector.addGoal(2, new MeleeAttackGoal(this, (double)1.0F, false));
        super.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F, 0.0F));
        super.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        super.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        super.targetSelector.removeAllGoals(goal -> true);
        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        super.targetSelector.addGoal(1, new HurtByTargetGoal(this, Skeleton.class, Stray.class));
        super.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal(this, false));
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        boolean bool = super.hurtServer(level, damageSource, amount);
        this.setCustomName(NMSMobUtil.generateHealthText(getMobName(), this));
        return bool;
    }


    @Override
    public Component getMobName() {
        return Component.literal("Angry Enderman").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.WHITE.value())));
    }

    @Override
    public boolean teleport() {
        return false;
    }

}
