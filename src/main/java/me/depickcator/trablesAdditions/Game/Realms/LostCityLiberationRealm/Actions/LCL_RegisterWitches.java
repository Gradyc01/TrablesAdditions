package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;

public class LCL_RegisterWitches extends LCL_RegisterSpawns {
    public LCL_RegisterWitches(String meshName, RealmController controller, Collection<LivingEntity> entities) {
        super(meshName, controller, entities);
    }

    @Override
    public boolean start() {
        if (new LCL_SpawnIn(meshName, controller).start()) {
            try {
                LocationMesh roomMesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
                Random random = new Random();
                LivingEntity entity = getMob(5, roomMesh.getRandomLocationsFromMesh(random, 1, false).getFirst(), random);
                entity.setGlowing(true);
                return true;
            } catch (IOException e) {
                TextUtil.debugText("LCL Register Witches", e.getMessage());
                controller.stopRealm();
                return false;
            }
        }
        return false;
    }
}
