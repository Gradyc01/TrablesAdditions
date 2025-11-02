package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmWither;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.core.BlockPos;
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

public class LCL_Vex extends Vex implements RealmNMSMob {
    private int lifeTicks;
    private final int ticksBeforeDespawn = 20 * 20;
    public LCL_Vex(ServerLevel level) {
        super(EntityType.VEX, level);
//        this.setPosRaw(location.getX(), location.getY(), location.getZ());
//        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        this.setHealth(1);
        this.targetSelector.removeAllGoals(goal -> true);
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity source) {
        if (super.doHurtTarget(level, source)) {
            ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 2, false);
            if (event.callEvent()) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), event.getRadius(), false, Level.ExplosionInteraction.MOB);
            }
            this.remove(Entity.RemovalReason.DISCARDED, EntityRemoveEvent.Cause.DEATH);
            return true;
        };
        return false;
    }


    @Override
    public Component getMobName() {
        return Component.literal("Vex").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.PINK.value())));
    }
}
