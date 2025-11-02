package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs;

import me.depickcator.trablesAdditions.Game.Effects.Interfaces.ImmuneToEffects;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Util.NMSMobUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.scores.PlayerTeam;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

public class LCL_Evoker extends Evoker implements RealmNMSMob, ImmuneToEffects {
    public LCL_Evoker(Location location, int health) {
        super(EntityType.EVOKER, ((CraftWorld) location.getWorld()).getHandle());
        NMSMobUtil.setAndSpawn(this, location);
        goalSelector.removeAllGoals(goal -> true);
        super.goalSelector.addGoal(0, new FloatGoal(this));
        super.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6, (double)1.0F));
        super.goalSelector.addGoal(4, new EvokerSummonSpellGoal());
        super.goalSelector.addGoal(5, new EvokerAttackSpellGoal());
        super.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 16.0F, 1.0F));
        targetSelector.removeAllGoals(goal -> true);
        super.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Mob.class)));
        super.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(300));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(35);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
        this.setHealth(this.getMaxHealth());
        this.getAttribute(Attributes.ARMOR).setBaseValue(10);
        this.setPersistenceRequired(true);
    }

    public LCL_Evoker(Location location) {
        this(location, 125);
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
        return Component.literal("True Sight Visionary").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.YELLOW.value())));
    }

    public class EvokerSummonSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
        private final TargetingConditions vexCountTargeting = TargetingConditions.forNonCombat().range((double)16.0F).ignoreLineOfSight().ignoreInvisibilityTesting();


        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                int size = getServerLevel(LCL_Evoker.this.level()).getNearbyEntities(Vex.class, this.vexCountTargeting, LCL_Evoker.this, LCL_Evoker.this.getBoundingBox().inflate((double)16.0F)).size();
                return LCL_Evoker.super.random.nextInt(8) + 1 > size;
            }
        }

        protected int getCastingTime() {
            return 100;
        }

        protected int getCastingInterval() {
            return 340;
        }

        protected void performSpellCasting() {
            ServerLevel serverLevel = (ServerLevel)LCL_Evoker.this.level();
            PlayerTeam team = LCL_Evoker.this.getTeam();

            for(int i = 0; i < 3; ++i) {
                BlockPos blockPos = LCL_Evoker.this.blockPosition().offset(-2 + LCL_Evoker.super.random.nextInt(5), 1, -2 + LCL_Evoker.super.random.nextInt(5));
//                Vex vex = (Vex)EntityType.VEX.create(LCL_Evoker.this.level(), EntitySpawnReason.MOB_SUMMONED);
                Vex vex = new LCL_Vex(serverLevel);
                if (vex != null) {
                    vex.snapTo(blockPos, 0.0F, 0.0F);
                    vex.finalizeSpawn(serverLevel, LCL_Evoker.this.level().getCurrentDifficultyAt(blockPos), EntitySpawnReason.MOB_SUMMONED, (SpawnGroupData)null);
                    vex.setOwner(LCL_Evoker.this);
                    vex.setBoundOrigin(blockPos);
                    vex.setLimitedLife(20 * (30 + LCL_Evoker.super.random.nextInt(90)));
                    if (team != null) {
                        serverLevel.getScoreboard().addPlayerToTeam(vex.getScoreboardName(), team);
                    }

                    serverLevel.addFreshEntityWithPassengers(vex, CreatureSpawnEvent.SpawnReason.SPELL);
                    serverLevel.gameEvent(GameEvent.ENTITY_PLACE, blockPos, GameEvent.Context.of(LCL_Evoker.this));
                }
            }

        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected SpellcasterIllager.IllagerSpell getSpell() {
            return IllagerSpell.SUMMON_VEX;
        }
    }

    class EvokerAttackSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
//        EvokerAttackSpellGoal() {
//            super(LCL_Evoker.this);
//        }
        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 100;
        }

        protected void performSpellCasting() {
            LivingEntity target = LCL_Evoker.this.getTarget();
            double min = Math.min(target.getY(), LCL_Evoker.this.getY());
            double d = Math.max(target.getY(), LCL_Evoker.this.getY()) + (double)1.0F;
            float f = (float) Mth.atan2(target.getZ() - LCL_Evoker.this.getZ(), target.getX() - LCL_Evoker.this.getX());
            if (LCL_Evoker.this.distanceToSqr(target) < (double)9.0F) {
                for(int i = 0; i < 5; ++i) {
                    float f1 = f + (float)i * (float)Math.PI * 0.4F;
                    this.createSpellEntity(LCL_Evoker.this.getX() + (double)Mth.cos(f1) * (double)1.5F, LCL_Evoker.this.getZ() + (double)Mth.sin(f1) * (double)1.5F, min, d, f1, 0);
                }

                for(int i = 0; i < 8; ++i) {
                    float f1 = f + (float)i * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                    this.createSpellEntity(LCL_Evoker.this.getX() + (double)Mth.cos(f1) * (double)2.5F, LCL_Evoker.this.getZ() + (double)Mth.sin(f1) * (double)2.5F, min, d, f1, 3);
                }
            } else {
                for(int i = 0; i < 16; ++i) {
                    double d1 = (double)1.25F * (double)(i + 1);
                    int i1 = 1 * i;
                    this.createSpellEntity(LCL_Evoker.this.getX() + (double)Mth.cos(f) * d1, LCL_Evoker.this.getZ() + (double)Mth.sin(f) * d1, min, d, f, i1);
                }
            }

        }

        private void createSpellEntity(double x, double z, double minY, double maxY, float yRot, int warmupDelay) {
            BlockPos blockPos = BlockPos.containing(x, maxY, z);
            boolean flag = false;
            double d = (double)0.0F;

            do {
                BlockPos blockPos1 = blockPos.below();
                BlockState blockState = LCL_Evoker.this.level().getBlockState(blockPos1);
                if (blockState.isFaceSturdy(LCL_Evoker.this.level(), blockPos1, Direction.UP)) {
                    if (!LCL_Evoker.this.level().isEmptyBlock(blockPos)) {
                        BlockState blockState1 = LCL_Evoker.this.level().getBlockState(blockPos);
                        VoxelShape collisionShape = blockState1.getCollisionShape(LCL_Evoker.this.level(), blockPos);
                        if (!collisionShape.isEmpty()) {
                            d = collisionShape.max(Direction.Axis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockPos = blockPos.below();
            } while(blockPos.getY() >= Mth.floor(minY) - 1);

            if (flag) {
                LCL_Evoker.this.level().addFreshEntity(new EvokerFangs(LCL_Evoker.this.level(), x, (double)blockPos.getY() + d, z, yRot, warmupDelay, LCL_Evoker.this));
                LCL_Evoker.this.level().gameEvent(GameEvent.ENTITY_PLACE, new Vec3(x, (double)blockPos.getY() + d, z), GameEvent.Context.of(LCL_Evoker.this));
            }

        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected SpellcasterIllager.IllagerSpell getSpell() {
            return IllagerSpell.FANGS;
        }
    }

}
