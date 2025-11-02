package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm;

import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.TrueSightKey;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Entities.ItemDisplayDetection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class TrueSightDisplay extends ItemDisplayDetection {
    private final LCL_Encounter encounter;
    public TrueSightDisplay(Location location, LCL_Encounter encounter) {
        super(location, TrueSightKey.getInstance().getResult());
        this.encounter = encounter;
    }

    @Override
    protected void uponPlayerWalkIn(Player player) {
    }
}
