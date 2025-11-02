package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmActions;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class LCL_Detonate extends RealmActions {
    public LCL_Detonate(String meshName, RealmController controller) {
        super(meshName, controller);
    }

    @Override
    public boolean start() {
        try {
            World world = controller.getWorld();
            LocationMesh mesh = controller.getReader().getLocationsMesh(meshName, world);
            Location location = mesh.getAllLocations().getFirst();
            for (Player player : location.getNearbyPlayers(10, 5)) {
                player.setVelocity(location.toVector().subtract(player.getLocation().toVector()).normalize().multiply(-1.5));
            }
            for (Location loc : mesh.getAllLocations()) {
                loc.createExplosion(5, false, true);
            }
            return true;
        } catch (IOException | NoSuchElementException e) {
            TextUtil.debugText("LCL Detonate", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
