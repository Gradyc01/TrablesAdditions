package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.*;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Skeletons.WitherRealmBog;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Skeletons.WitherRealmRangerSkeleton;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Skeletons.WitherRealmSkeleton;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Skeletons.WitherRealmSkeletonKnight;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Zombies.WitherRealmDrowned;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Zombies.WitherRealmHusk;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Zombies.WitherRealmZombie;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Zombies.WitherRealmZombieKnight;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Random;

public class WitherRealm_LoadRoom extends WitherRealmActions {
    public WitherRealm_LoadRoom(String meshName, RealmController controller) {
        super(meshName, controller);
    }

    public boolean start() {
        try {
            LocationMesh roomMesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            Random random = new Random();

            for (Pair<Location, Integer> spawnLoc : roomMesh.getAllLocationsWeighted()) {
                spawnMob(spawnLoc.getRight(), spawnLoc.getLeft(), random);
            }
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Wither Realm Load Room", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }

    private void spawnMob(int mobType, Location location, Random random) {
        location.getWorld().spawnParticle(Particle.SONIC_BOOM, location, 1, 0, 0, 0, 0);
        new BukkitRunnable() {
            @Override
            public void run() {
                switch (mobType) {
                    case 1 -> new WitherRealmZombie(location, random);
                    case 2 -> new WitherRealmSkeleton(location, random);
                    case 3 -> new WitherRealmSkeletonKnight(location);
                    case 4 -> new WitherRealmZombieKnight(location);
                    case 5 -> new WitherRealmEnderman(location, random);
                    case 6 -> new WitherRealmRangerSkeleton(location);
                    case 7 -> new WitherRealmHusk(location, random);
                    case 8 -> new WitherRealmBog(location, random);
                    case 9 -> new WitherRealmDrowned(location);
                }
            }
        }.runTaskLater(TrablesAdditions.getInstance(), 7);

    }
}
