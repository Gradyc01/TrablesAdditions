package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmActions;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.CleanseDisplay;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs.LCL_ZombieKnight;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class LCL_AddCleanseLocations extends RealmActions {
    private final Entity entity;
    private final LCL_Encounter1 encounter;
    public LCL_AddCleanseLocations(String meshName, RealmController controller, Entity entity, LCL_Encounter1 encounter) {
        super(meshName, controller);
        this.entity = entity;
        this.encounter = encounter;
    }

    @Override
    public boolean start() {
        try {
            World world = controller.getWorld();
            LocationMesh mesh = controller.getReader().getLocationsMesh(meshName, world);
            for (Pair<Location, Integer> loc : mesh.getAllLocationsWeighted()) {
                boolean fakeCleanse = entity instanceof LCL_ZombieKnight ? loc.getRight() == 2 : loc.getRight() == 1;
                new CleanseDisplay(loc.getLeft(), encounter, fakeCleanse);
            }
            return true;
        } catch (IOException | NoSuchElementException e) {
            TextUtil.debugText("Add Cleanse Location", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
