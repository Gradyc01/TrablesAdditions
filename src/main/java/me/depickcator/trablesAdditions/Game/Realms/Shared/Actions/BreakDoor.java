package me.depickcator.trablesAdditions.Game.Realms.Shared.Actions;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmActions;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.List;

public class BreakDoor extends RealmActions {
    public BreakDoor(String meshName, RealmController controller) {
        super(meshName, controller);
    }

    public boolean start() {
        try {
            LocationMesh mesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
//            for (Location location : mesh.getAllLocations()) {
//                Block block = location.getBlock();
//                block.setType(Material.BEDROCK);
//                block.breakNaturally(true);
//            }
            breakDoor(mesh.getAllLocations());
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Action Break Door", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }

    protected void breakDoor(List<Location> locations) {
        for (Location location : locations) {
            Block block = location.getBlock();
            block.setType(Material.BEDROCK);
            block.breakNaturally(true);
        }
    }
}
