package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Map;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI.WitherRealmLaunchControlPanelGUI;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI.WitherRealm_LootGUI;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Persistence.RealmMeshReader;
import me.depickcator.trablesAdditions.Scoreboards.WitherRealmBoard;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.BoundingBox;

import java.io.IOException;
import java.util.List;

public class WitherRealmMap implements ScoreboardObserver {
    private final String meshName;
    private final String chestMapping;
    private final RealmController controller;
    private final WitherRealm realm;
    private final BoundingBox boundingBox;
    private final String displayName;
    private int lootedChests;
    private int totalChests;
    public WitherRealmMap(String displayName, String meshName, String chestMapping, RealmController controller, WitherRealm realm) {
        this.meshName = meshName;
        this.chestMapping = chestMapping;
        this.controller = controller;
        this.realm = realm;
        this.boundingBox = initBoundingBox();
        this.displayName = displayName;

        WitherRealmBoard.getInstance().addObserver(this);
    }

    private BoundingBox initBoundingBox() {
        try {
            RealmMeshReader reader = controller.getReader();
            LocationMesh roomMesh = reader.getLocationsMesh(meshName, controller.getWorld());
            List<Location> locations = roomMesh.getAllLocations();
            if (locations.size() < 2) {
                TextUtil.debugText("Room Mesh has less than 2 locations.");
                controller.stopRealm();
            }
            Location pos1 = locations.getFirst();
            Location pos2 = locations.get(1);
            return new BoundingBox(pos1.x(), pos1.y(), pos1.z(), pos2.x(), pos2.y(), pos2.z());
        } catch (IOException e) {
            TextUtil.debugText("Wither Realm Control Panel", e.getMessage());
            controller.stopRealm();
            return new BoundingBox();
        }
    }


    @Override
    public void update(BoardMaker maker, Objective board, PlayerData playerData) {
        if (chestMapping.isEmpty()) {
            maker.editLine(board,5, TextUtil.makeText(""));
        } else {
            if (totalChests == 0) updateChestCount();
            maker.editLine(board, 5, TextUtil.makeText( lootedChests + "/" + totalChests + " Chests Looted", TextUtil.YELLOW));
        }
        maker.editLine(board, 6, TextUtil.makeText("◈ " + displayName, TextUtil.AQUA));
    }

    public void updateChestCount() {
        totalChests = 0;
        lootedChests = 0;
        for (WitherRealm_LootGUI gui : realm.getLootChestGUIs(chestMapping)) {
            if (gui.isHasBeenFound()) lootedChests++;
            totalChests++;
        }
    }



    @Override
    public String observerName() {
        return meshName + " Map";
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void delete() {
        WitherRealmBoard.getInstance().removeObserver(this);
    }
}
