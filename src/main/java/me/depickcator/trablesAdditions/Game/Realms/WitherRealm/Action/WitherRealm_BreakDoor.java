package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.IOException;

public class WitherRealm_BreakDoor extends WitherRealmActions {
    public WitherRealm_BreakDoor(String meshName, RealmController controller) {
        super(meshName, controller);
    }

    public boolean start() {
        try {
            LocationMesh mesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            for (Location location : mesh.getAllLocations()) {
                Block block = location.getBlock();
                block.setType(Material.BEDROCK);
                block.breakNaturally(true);
            }
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Wither Realm Break Door", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
