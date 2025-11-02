package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Actions.BreakDoor;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class LCL_BreakDoor extends BreakDoor {
    public LCL_BreakDoor(String meshName, RealmController controller) {
        super(meshName, controller);
    }

    @Override
    public boolean start() {
        try {
            World world = controller.getWorld();
            LocationMesh mesh = controller.getReader().getLocationsMesh(meshName, world);
            Location loc1 = mesh.getAllLocations().getFirst();
            Location loc2 = mesh.getAllLocations().getLast();
            for (int y = Math.min(loc1.getBlockY(), loc2.getBlockY()); y <= Math.max(loc1.getBlockY(), loc2.getBlockY()); y++) {
                for (int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ());  z <= Math.max(loc1.getBlockZ(), loc2.getBlockZ()); z++) {
                    for (int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x <= Math.max(loc1.getBlockX(), loc2.getBlockX()); x++) {
                        Location loc = new Location(world, x, y, z);
                        breakDoor(List.of(loc));
                    }
                }
            }
            return true;
        } catch (IOException | NoSuchElementException e) {
            TextUtil.debugText("Action Break Door", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
