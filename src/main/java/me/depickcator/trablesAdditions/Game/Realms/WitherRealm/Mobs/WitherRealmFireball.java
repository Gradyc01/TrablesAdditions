package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.jetbrains.annotations.Nullable;

public class WitherRealmFireball extends LargeFireball {
    private final boolean canBeReflected;

    public WitherRealmFireball(Level level, LivingEntity owner, Vec3 movement, int explosionPower, boolean canBeReflected) {
        super(level, owner, movement, explosionPower);
        this.canBeReflected = canBeReflected;
    }

    public WitherRealmFireball(Level level, LivingEntity owner, Vec3 movement, int explosionPower) {
        this(level, owner, movement, explosionPower, true);
    }

    public WitherRealmFireball(Level level, Player player, Vec3 movement, int explosionPower) {
        this(level, ((CraftLivingEntity) player).getHandle(), movement, explosionPower, false);
        this.setOwner(((CraftLivingEntity) player).getHandle());
        Location location = player.getEyeLocation().add(movement.x(), movement.y(), movement.z());
        this.setPosRaw(location.getX(), location.getY(), location.getZ());
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
        if (result.getEntity().equals(getOwner())) return;
        super.onHitEntity(result);
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity entity, @Nullable EntityReference<Entity> owner, boolean deflectionByPlayer) {
        TextUtil.debugText("Deflected");
        if (this.canBeReflected && deflectionByPlayer) {
            return super.deflect(deflection, entity, owner, true);
        }
        return false;
    }
}
