package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmSkeleton;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmZombie;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

public class WitherRealm_LoadDoor extends WitherRealmActions{
//    private final Set<WitherRealm_LoadRoom> triggeredRooms; /*The set of roms that would get loaded if this door were to be broken*/
    public WitherRealm_LoadDoor(String meshName, RealmController controller) {
        super(meshName, controller);
    }

    public boolean start() {
        try {
            LocationMesh mesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            for (Location location : mesh.getAllLocations()) {
                Block block = location.getBlock();
                block.setType(Material.INFESTED_CRACKED_STONE_BRICKS);
                block.setMetadata(WitherRealm.WITHER_REALM_DUNGEON_DOOR_KEY, new FixedMetadataValue(TrablesAdditions.getInstance(), meshName));
            }
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Wither Realm Load Room", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }

    private void changeBlock(Block block) {
        block.setType(Material.INFESTED_CRACKED_STONE_BRICKS);
        block.setMetadata(WitherRealm.WITHER_REALM_DUNGEON_DOOR_KEY, new FixedMetadataValue(TrablesAdditions.getInstance(), meshName));
    }
}
