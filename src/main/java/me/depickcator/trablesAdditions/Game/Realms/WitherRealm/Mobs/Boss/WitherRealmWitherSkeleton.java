package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Boss;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmWither;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.Random;

public class WitherRealmWitherSkeleton extends WitherSkeleton implements RealmNMSMob {
    private final WitherRealmWither wither;
    public WitherRealmWitherSkeleton(Location location, Random random, WitherRealmWither wither) {
        super(EntityType.WITHER_SKELETON, ((CraftWorld) location.getWorld()).getHandle());
        this.setPosRaw(location.getX() + random.nextInt(-5, 6),
                location.getY(),
                location.getZ() + random.nextInt(-5, 6));
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        this.wither = wither;
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
    public Component getMobName() {
        return Component.literal("Krivon's Disciple").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(TextUtil.PINK.value())));
    }
}
