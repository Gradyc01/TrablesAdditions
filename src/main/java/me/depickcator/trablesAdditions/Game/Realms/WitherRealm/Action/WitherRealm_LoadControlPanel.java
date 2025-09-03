package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Persistence.RealmMeshReader;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI.WitherRealmControlPanelGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Random;

public class WitherRealm_LoadControlPanel extends WitherRealmActions {
    public WitherRealm_LoadControlPanel(String meshName, RealmController controller) {
        super(meshName, controller);
    }

    public boolean start() {
        try {
            RealmMeshReader reader = controller.getReader();
            LocationMesh roomMesh = reader.getLocationsMesh(meshName, controller.getWorld());
            Location loc = roomMesh.getRandomLocationsFromMesh(new Random(), 1, true).getFirst();
            Block block = loc.getBlock();
            block.setType(Material.COMMAND_BLOCK);
            new WitherRealmControlPanelGUI(block,
                    controller,
                    reader.getLocationsMesh("spawn_crystal_center", controller.getWorld()).getAllLocations().getFirst(),
                    reader.getLocationsMesh("spawn_crystal", controller.getWorld()).getAllLocations());
            return true;
        } catch (Exception e) {
            TextUtil.debugText("Wither Realm Control Panel", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
