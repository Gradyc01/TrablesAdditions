package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs.LCL_E1Evoker;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs.LCL_Evoker;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.entity.LivingEntity;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;

public class LCL_RegisterEvoker extends LCL_SpawnIn {
    private final LCL_Encounter1 encounter;
    public LCL_RegisterEvoker(String meshName, RealmController controller, LCL_Encounter1 encounter1) {
        super(meshName, controller);
        this.encounter = encounter1;
    }

    @Override
    public boolean start() {
        try {
            LocationMesh roomMesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            Random random = new Random();
            LivingEntity entity = new LCL_E1Evoker(roomMesh.getRandomLocationsFromMesh(random, 1, false).getFirst(), encounter)
                    .getBukkitLivingEntity();
            entity.setGlowing(true);
            return true;
        } catch (IOException e) {
            TextUtil.debugText("LCL Register Evoker", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
