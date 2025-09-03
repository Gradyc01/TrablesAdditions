package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EnderCrystal;

import java.util.List;

public class WitherRealmEndCrystal extends EndCrystal {
    private final Level level;
    public WitherRealmEndCrystal(Location startingPoint, Location endPoint, List<WitherRealmEndCrystal> crystals) {
        super(EntityType.END_CRYSTAL, ((CraftWorld) startingPoint.getWorld()).getHandle());
        this.setPosRaw(startingPoint.getX(), startingPoint.getY(), startingPoint.getZ());
        ((CraftWorld) startingPoint.getWorld()).getHandle().addFreshEntity(this);
        level = ((CraftWorld) startingPoint.getWorld()).getHandle();
        this.setSilent(true);
        this.setInvulnerable(true);
        this.setInvisible(true);
        this.setShowBottom(false);
        this.setBeamTarget(new BlockPos(endPoint.getBlockX(), endPoint.getBlockY(), endPoint.getBlockZ()));
        crystals.add(this);
    }

    public void ignite() {
        this.remove(RemovalReason.DISCARDED);
        level.explode(this,  this.getX(), this.getY(), this.getZ(), 3, false, Level.ExplosionInteraction.MOB);
    }

}
