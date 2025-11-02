package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmActions;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.CleanseDisplay;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs.LCL_ZombieKnight;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.NoSuchElementException;

public class LCL_AddAnswerLocations extends RealmActions {
    private final LCL_Encounter1 encounter;

    public LCL_AddAnswerLocations(String meshName, RealmController controller, LCL_Encounter1 encounter) {
        super(meshName, controller);
        this.encounter = encounter;
    }

    @Override
    public boolean start() {
        try {
            World world = controller.getWorld();
            LocationMesh mesh = controller.getReader().getLocationsMesh(meshName, world);
            for (Location loc : mesh.getAllLocations()) {
                new TriggerAnswerPlacement(loc.getBlock(), encounter);
            }
            return true;
        } catch (IOException | NoSuchElementException e) {
            TextUtil.debugText("Add Answer Location", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
