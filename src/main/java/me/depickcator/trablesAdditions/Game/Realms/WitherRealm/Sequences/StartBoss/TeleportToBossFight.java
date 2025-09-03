package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Sequences.StartBoss;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.GameLauncher;
import me.depickcator.trablesAdditions.Interfaces.GameSequences;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TeleportToBossFight extends GameSequences {
    private final String meshName;
    private final RealmController controller;
    public TeleportToBossFight(String meshName, RealmController controller) {
        super("Teleport to boss fight");
        this.meshName = meshName;
        this.controller = controller;
    }

    @Override
    public void run(GameLauncher game) {
        try {
            LocationMesh mesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            teleportPlayers(mesh.getAllLocationsWeighted(), controller.getPlayingPlayers());
            game.callback();
        } catch (Exception e) {
            TextUtil.debugText(this.getSequenceName(), e.getMessage());
            controller.stopRealm();
        }
    }

    private void teleportPlayers(List<Pair<Location, Integer>> locations, List<Player> players) {
        if (locations.isEmpty()) throw new IllegalArgumentException("Locations list is empty");
        Iterator<Pair<Location, Integer>> iter = locations.iterator();
        for (Player player : players) {
            Pair<Location, Integer> location ;
            if (iter.hasNext()) {
                location = iter.next();
            } else {
                iter = locations.iterator();
                location = locations.getFirst();
            }
            Location loc = location.getLeft().clone();
            loc.setRotation(90 * (location.getRight() - 1), 0);
            player.teleport(loc);
        }
    }
}
