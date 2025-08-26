package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.*;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.world.entity.EntityType;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;

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
                switch (spawnLoc.getRight()) {
                    case 1 -> new WitherRealmZombie(spawnLoc.getLeft(), random);
                    case 2 -> new WitherRealmSkeleton(spawnLoc.getLeft(), random);
                    case 3 -> new WitherRealmSkeletonKnight(spawnLoc.getLeft());
                    case 4 -> new WitherRealmZombieKnight(spawnLoc.getLeft());
                    case 5 -> new WitherRealmEnderman(spawnLoc.getLeft(), random);
                    case 6 -> new WitherRealmRangerSkeleton(spawnLoc.getLeft());
                    case 7 -> new WitherRealmHusk(spawnLoc.getLeft(), random);
                }
            }
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Wither Realm Load Room", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
