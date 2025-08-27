package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.*;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;

import java.io.IOException;
import java.util.Random;

public class WitherRealm_LoadMiniBossRoom extends WitherRealmActions {
    public WitherRealm_LoadMiniBossRoom(String meshName, RealmController controller) {
        super(meshName, controller);
    }

    public boolean start() {
        try {
            LocationMesh roomMesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
             new WitherRealmZombieKnight(roomMesh.getAllLocationsWeighted().getFirst().getLeft());

            return true;
        } catch (Exception e) {
            TextUtil.debugText("Wither Realm Load Room", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
