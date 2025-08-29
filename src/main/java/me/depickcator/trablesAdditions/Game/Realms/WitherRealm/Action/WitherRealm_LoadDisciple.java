package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Zombies.WitherRealmDrowned;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class WitherRealm_LoadDisciple extends WitherRealmActions {
    private final Location location;
    public WitherRealm_LoadDisciple(String meshName, RealmController controller, WitherRealm realm) {
        super(meshName, controller);
        realm.addDisciple(meshName);
        location = load();

    }

    private Location load() {
        try {
            LocationMesh roomMesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
//            for (Pair<Location, Integer> loc : roomMesh.getAllLocationsWeighted()) {
//                for (int i = 0; i < loc.getRight(); i++) {
//                    locations.add(loc.getLeft());
//                }
//            }
            return roomMesh.getRandomLocationsFromMesh(new Random(), 1, true).getFirst();
        } catch (Exception e) {
            TextUtil.debugText("Wither Realm Disciple Room", e.getMessage());
            controller.stopRealm();
            return null;
        }
    }

    public boolean start() {
        LivingEntity entity = new WitherRealmDrowned(location).getBukkitLivingEntity();
        entity.setMetadata(WitherRealm.WITHER_REALM_DUNGEON_DISCIPLE_KEY, new FixedMetadataValue(TrablesAdditions.getInstance(), meshName));
        return true;
    }
}
