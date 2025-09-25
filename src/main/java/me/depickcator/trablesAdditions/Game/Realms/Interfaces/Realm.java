package me.depickcator.trablesAdditions.Game.Realms.Interfaces;

import me.depickcator.trablesAdditions.Game.Effects.FloodBlocks;
import me.depickcator.trablesAdditions.Game.Effects.Interfaces.Floodable;
import me.depickcator.trablesAdditions.Game.Effects.PortalFrameRemover;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Listeners.DimensionalTravel;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import me.depickcator.trablesAdditions.Util.TextUtil;
import me.depickcator.trablesAdditions.Util.WorldEditUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Orientable;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public abstract class Realm implements Floodable {
    private final Location portalLocation;
//    private final BlockFace portalBlockFace;
    private final String REALM_NAME;
    private final String DISPLAY_NAME;
    private RealmStates realmState;
    private final UUID uuid;
    public Realm(Location portalLocation, String realmName, String displayName) {
        this.portalLocation = portalLocation;
//        this.portalBlockFace = facing;
        this.REALM_NAME = realmName;
        this.DISPLAY_NAME = displayName;
        this.uuid = UUID.randomUUID();
//        this.realmState = getStartingRealmState();

    }

    public void openPortal() {
        Location portalLocation = getPortalLocation();
        World portalWorld = portalLocation.getWorld();
        portalWorld.spawnParticle(Particle.EXPLOSION, portalLocation.clone().add(0, 2,0), 200, 5, 5, 5);
        portalWorld.playSound(portalLocation, Sound.ENTITY_GENERIC_EXPLODE, 5, 1f);
        File schem = new File(getPortalSchemFilePath());
        WorldEditUtil.pasteSchematic(schem, portalLocation);
    };

    public void closePortal() {
        new FloodBlocks(getPortalLocation(), 1, new PortalFrameRemover()).autoFlood(new Random());
    };

    public Location getPortalLocation() {
        return portalLocation.clone();
    }

    public abstract void initialize(PlayerData playerData);
    public abstract void onStart(RealmController controller);
    public abstract void onLoop(RealmController controller);
    public abstract void onEnd(RealmController controller);
    protected abstract RealmStates getStartingRealmState();
    public abstract void worldRules(World world);
    public abstract void onStartBoss(RealmController controller);
    public abstract void onBossDefeated(RealmController controller);
    public abstract BoardMaker getBoardMaker();

    public String getWorldName() {
        return REALM_NAME;
    }

    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    public String getWorldFilePath() {
        return "./plugins/TrablesAdditions/Realms/" + getWorldName() + "/world";
    }

    public String getMeshFilePath() {
        return "./plugins/TrablesAdditions/Realms/" + getWorldName() + "/mesh.json";
    }

    public String getPortalSchemFilePath() {
        return "./plugins/TrablesAdditions/Realms/" + getWorldName() + "/portal.schem";
    }

    public RealmStates getRealmState() {
        return realmState;
    }

    public void setRealmState(RealmStates realmState) {
        if (this.realmState != null) this.realmState.onRemove();
        TextUtil.debugText("Realm", REALM_NAME + " changed state from " +
                ((this.realmState != null) ?  this.realmState.getStateName() : "None")
                + " to " + realmState.getStateName());

        this.realmState = realmState;
        this.realmState.onSet();
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Block changeBlock(Block block, Random r, FloodBlocks floodBlocks) {
        block.setType(floodBlocks.chooseNextMaterial(r));
        return block;
    }

    @Override
    public boolean isUnFloodable(Block b) {
        return getUnFloodables().containsKey(b.getType());
    }

    @Override
    public double getNewSuccessRate(double oldSuccessRate, Random r) {
        return oldSuccessRate - r.nextDouble(0.1, 0.20);
    }
}
