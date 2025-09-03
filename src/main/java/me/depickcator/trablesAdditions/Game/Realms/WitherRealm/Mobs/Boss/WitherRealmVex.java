package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Boss;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmWither;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.Random;

public class WitherRealmVex extends Vex implements RealmNMSMob {
    private final WitherRealmWither wither;
    public WitherRealmVex(Location location, Random random, WitherRealmWither wither) {
        super(EntityType.VEX, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX() + random.nextInt(-5, 6),
                location.getY() + random.nextInt(-5, 6),
                location.getZ() + random.nextInt(-5, 6));
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        this.wither = wither;
        this.targetSelector.removeAllGoals(goal -> true);
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void aiStep() {
        if (!wither.isAlive()) {
            this.remove(RemovalReason.DISCARDED);
        } else {
            super.aiStep();
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity source) {
        if (super.doHurtTarget(level, source)) {
            ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 2, false);
            if (event.callEvent()) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), event.getRadius(), false, Level.ExplosionInteraction.MOB);
            }
            this.remove(RemovalReason.DISCARDED, EntityRemoveEvent.Cause.DEATH);
            return true;
        };
        return false;
    }


    @Override
    public Component getMobName() {
        return Component.literal("Krivon's Disciple").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.PINK.value())));
    }
}
