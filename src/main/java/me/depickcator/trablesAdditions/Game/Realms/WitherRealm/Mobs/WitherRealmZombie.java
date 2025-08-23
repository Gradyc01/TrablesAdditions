package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WitherRealmZombie extends Zombie {
    private final List<ItemStack> loot;
    private final Random random;

    public WitherRealmZombie(Location location, Random random) {
        super(EntityType.ZOMBIE, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        this.random = random;
        giveAttributes();
        loot = new ArrayList<>(List.of(new ItemStack(Material.ROTTEN_FLESH)));
//        for (Goal goal : super.targetSelector.)
//        super.targetSelector.removeGoal(new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[]{ZombifiedPiglin.class}));
        super.targetSelector.removeAllGoals(goal -> true);
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        super.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
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
    protected void handleAttributes(float difficulty) {
        this.randomizeReinforcementsChance();
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addOrReplacePermanentModifier(new AttributeModifier(Mob.RANDOM_SPAWN_BONUS_ID, super.random.nextDouble() * (double)0.05F, AttributeModifier.Operation.ADD_VALUE));
        double d = super.random.nextDouble() * (double)1.5F * (double)difficulty;
        if (d > (double)1.0F) {
            this.getAttribute(Attributes.FOLLOW_RANGE).addOrReplacePermanentModifier(new AttributeModifier(Mob.RANDOM_SPAWN_BONUS_ID, 15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(Mob.RANDOM_SPAWN_BONUS_ID, 30, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        this.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(Mob.RANDOM_SPAWN_BONUS_ID, 2.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier(Mob.RANDOM_SPAWN_BONUS_ID, 14, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0F)
                .add(Attributes.ARMOR, 2.0F)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    private void giveAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25F);
        this.getAttribute(Attributes.ARMOR).setBaseValue(15.0F);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(25.0F);
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(25.0F);
        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0F);
        this.setShouldBurnInDay(false);
        this.setPersistenceRequired(true);
    }

//    @Override
//    protected boolean shouldDropLoot() {
//        return false;
//    }
}
