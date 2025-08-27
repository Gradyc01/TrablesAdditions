package me.depickcator.trablesAdditions.Game.Realms.Interfaces;

import me.depickcator.trablesAdditions.Game.Effects.FloodBlocks;
import me.depickcator.trablesAdditions.Game.Effects.PortalFrameRemover;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import me.depickcator.trablesAdditions.Util.TextUtil;
import me.depickcator.trablesAdditions.Util.WorldEditUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

import java.io.File;
import java.util.Random;

public abstract class Realm implements TrablesMenuActionable {
    private final Location portalLocation;
    private final String REALM_NAME;
    private final String DISPLAY_NAME;
    private RealmStates realmState;
    public Realm(Location portalLocation, String realmName,  String displayName) {
        this.portalLocation = portalLocation;
        this.REALM_NAME = realmName;
        this.DISPLAY_NAME = displayName;
        this.realmState = getStartingRealmState();
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

    public abstract void onStart(RealmController controller);
    public abstract void onLoop(RealmController controller);
    protected abstract RealmStates getStartingRealmState();
    public abstract void worldRules(World world);

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
        TextUtil.debugText("Realm", REALM_NAME + " changed state from " + this.realmState.getStateName() + " to " + realmState.getStateName());
        this.realmState = realmState;
    }


}
