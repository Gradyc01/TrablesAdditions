package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmEndCrystal;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmWither;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealmBossFight;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Persistence.RealmMeshReader;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI.WitherRealmBossControlPanelGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Wither_BossState_Phase0 extends Wither_BossState {
    public Wither_BossState_Phase0(WitherRealm realm, RealmController controller) {
        super(realm, controller, new WitherRealmBossFight(realm, controller));
    }

    @Override
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSet() {
        super.onSet();
        RealmMeshReader reader = getController().getReader();
        try {
            Location loc = spawnWither(reader.getLocationsMesh("room_boss_spawn", getController().getWorld()));
            spawnControlPanel("rig_bl", "ui_boss_bl", loc);
            spawnControlPanel("rig_br", "ui_boss_br", loc);
            spawnControlPanel("rig_ur", "ui_boss_ur", loc);
            spawnControlPanel("rig_ul", "ui_boss_ul", loc);
        } catch (IOException e) {
            TextUtil.debugText(this.getStateName() + "!", "Error reading mesh reader " + e.getMessage());
            getController().stopRealm();
        }
    }

    @Override
    public boolean canOpenPanel() {
        return false;
    }

    @Override
    public void setNextBossState() {
        getRealm().setRealmState(new Wither_BossState_Phase1(getRealm(), getController(), getBossFight()));
    }

    @Override
    public String getStateName() {
        return "Boss Phase 0";
    }

    private Location spawnWither(LocationMesh locationMesh) {
        WitherRealmWither wither = new WitherRealmWither(locationMesh.getAllLocations().getFirst(), getBossFight());
        return wither.getLocation();
    }

    private void spawnControlPanel(String rigMeshName, String uiMeshName, Location witherLocation) {
        RealmMeshReader reader = getController().getReader();

        try {
            LocationMesh mesh = reader.getLocationsMesh(uiMeshName, getController().getWorld());
            Location panelLocation = null;
            Location crystalLocation = null;
            for (Pair<Location, Integer> pair : mesh.getAllLocationsWeighted()) {
                if (pair.getRight() == 1) panelLocation = pair.getLeft();
                if (pair.getRight() == 2) crystalLocation = pair.getLeft();
            }
            if (panelLocation == null || crystalLocation == null) throw new IllegalArgumentException("No panel or no crystal location");
            Block block = panelLocation.getBlock();
            block.setType(Material.COMMAND_BLOCK);
            new WitherRealmBossControlPanelGUI(block,
                    new WitherRealmEndCrystal(crystalLocation, witherLocation, new ArrayList<>()),
                    getController(),
                    rigMeshName);

        } catch (Exception e) {
            TextUtil.debugText(this.getStateName(), "Error! reading mesh reader " + e.getMessage() + e.getClass().getSimpleName());
        }

    }
}
