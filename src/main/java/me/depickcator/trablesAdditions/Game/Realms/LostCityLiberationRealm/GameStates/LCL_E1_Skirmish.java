package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;

import java.util.List;

public class LCL_E1_Skirmish extends LCL_Encounter1State {

    public LCL_E1_Skirmish(LostCityLiberationRealm realm, LCL_Encounter1 encounter) {
        super(realm, encounter);
    }

    @Override
    public LCL_Encounter1State getNextState() {
        return new LCL_E1_Witches(getRealm(), getEncounter());
    }

    @Override
    public String getStateName() {
        return "Encounter 1: Skirmish";
    }
}
