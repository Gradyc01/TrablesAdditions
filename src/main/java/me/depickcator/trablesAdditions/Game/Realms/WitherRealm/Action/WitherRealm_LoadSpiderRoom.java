package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Spider.WitherRealmCaveSpider;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Spider.WitherRealmSpider;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;

import java.io.IOException;
import java.util.Random;

public class WitherRealm_LoadSpiderRoom extends WitherRealmActions {
    public WitherRealm_LoadSpiderRoom(String meshName, RealmController controller) {
        super(meshName, controller);
    }

    public boolean start() {
        try {
            LocationMesh roomMesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            Random random = new Random();

            for (Pair<Location, Integer> spawnLoc : roomMesh.getAllLocationsWeighted()) {
                switch (spawnLoc.getRight()) {
                    case 1 -> new WitherRealmSpider(spawnLoc.getLeft(), random);
                    case 2 -> new WitherRealmCaveSpider(spawnLoc.getLeft(), random);
                }
            }
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Wither Realm Load Spider Room", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
