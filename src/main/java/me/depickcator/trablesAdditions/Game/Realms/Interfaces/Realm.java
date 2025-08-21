package me.depickcator.trablesAdditions.Game.Realms.Interfaces;

import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import org.bukkit.Location;

public abstract class Realm implements TrablesMenuActionable {
    private final Location portalLocation;
    private final String REALM_NAME;
    public Realm(Location portalLocation, String realmName) {
        this.portalLocation = portalLocation;
        this.REALM_NAME = realmName;
    }

    public abstract void openPortal();

    public abstract void closePortal();

    public Location getPortalLocation() {
        return portalLocation.clone();
    }

    public String getWorldName() {
        return REALM_NAME;
    }

    public String getWorldFilePath() {
        return "./plugins/TrablesAdditions/Realms/" + getWorldName() + "/world";
    }

    public String getPortalSchemFilePath() {
        return "./plugins/TrablesAdditions/Realms/" + getWorldName() + "/portal.schem";
    }
}
