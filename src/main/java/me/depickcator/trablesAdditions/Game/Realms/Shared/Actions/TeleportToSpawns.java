package me.depickcator.trablesAdditions.Game.Realms.Shared.Actions;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmActions;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Random;

public class TeleportToSpawns extends RealmActions {
    private final Player player;
    public TeleportToSpawns(String meshName, RealmController controller, Player player) {
        super(meshName, controller);
        this.player = player;
    }

    public boolean start() {
        try {
            Pair<Location, Integer> pair = controller.getReader().getLocationsMesh(meshName, controller.getWorld())
                    .getRandomLocationsWeightedFromMesh(new Random(), 1, true).getFirst();
            Location loc = pair.getLeft().clone();
            player.teleport(loc);
            player.setRotation(90 * pair.getRight(), 0);
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Action Break Door", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
