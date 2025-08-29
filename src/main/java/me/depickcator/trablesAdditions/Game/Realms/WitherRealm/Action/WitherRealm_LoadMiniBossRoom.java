package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Minibosses.WitherRealmLaunchGolem;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.Minibosses.WitherRealmSummonGolem;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class WitherRealm_LoadMiniBossRoom extends WitherRealmActions {
    private final Location location;
    private final Random random;
    public WitherRealm_LoadMiniBossRoom(String meshName, RealmController controller, WitherRealm realm) {
        super(meshName, controller);
        random = new Random();
        realm.addDisciple(meshName);
        location = load();
    }

    private Location load() {
        try {
            LocationMesh roomMesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            return roomMesh.getRandomLocationsFromMesh(random, 1, true).getFirst();
        } catch (Exception e) {
            TextUtil.debugText("Wither Realm Disciple Room", e.getMessage());
            controller.stopRealm();
            return null;
        }
    }

    public boolean start() {
//        LivingEntity entity = new WitherRealmDrowned(location).getBukkitLivingEntity();
        LivingEntity entity = switch (random.nextInt(2)) {
            case 0 -> new WitherRealmLaunchGolem(location, random).getBukkitLivingEntity();
            default -> new WitherRealmSummonGolem(location, random).getBukkitLivingEntity();
        };
        entity.setMetadata(WitherRealm.WITHER_REALM_DUNGEON_DISCIPLE_KEY, new FixedMetadataValue(TrablesAdditions.getInstance(), meshName));
        return true;
    }
}
