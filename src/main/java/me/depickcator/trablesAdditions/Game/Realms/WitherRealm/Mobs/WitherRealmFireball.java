package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class WitherRealmFireball extends LargeFireball {
    public WitherRealmFireball(Level level, LivingEntity owner, Vec3 movement, int explosionPower) {
        super(level, owner, movement, explosionPower);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), (float) this.explosionPower /4, false);
        if (event.callEvent()) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), event.getRadius(), event.getFire(), Level.ExplosionInteraction.MOB);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
    }
}
