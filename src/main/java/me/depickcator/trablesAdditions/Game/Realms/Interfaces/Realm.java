package me.depickcator.trablesAdditions.Game.Realms.Interfaces;

import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import org.bukkit.Location;

public abstract class Realm implements TrablesMenuActionable {
    private final Location portalLocation;
    private final String WORLD_NAME;
    public Realm(Location portalLocation, String worldName) {
        this.portalLocation = portalLocation;
        this.WORLD_NAME = worldName;
    }

    public abstract void openPortal();

    public abstract void closePortal();

    public Location getPortalLocation() {
        return portalLocation.clone();
    }

    public String getWorldName() {
        return WORLD_NAME;
    }
}
