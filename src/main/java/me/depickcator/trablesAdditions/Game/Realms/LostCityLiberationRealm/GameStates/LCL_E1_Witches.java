package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.event.entity.EntityDeathEvent;

public class LCL_E1_Witches extends LCL_Encounter1State {
    public LCL_E1_Witches(LostCityLiberationRealm realm, LCL_Encounter1 encounter) {
        super(realm, encounter);
    }

    @Override
    public LCL_Encounter1State getNextState() {
        return new LCL_E1_Evoker(getRealm(), getEncounter());
    }

    @Override
    public void onSet() {
        super.onSet();
        getEncounter().spawnMobWave(3, true);
        getEncounter().getAudience().sendMessage(
                TextUtil.makeText("The Three sisters have come to defend the gateway", TextUtil.YELLOW, false, true));
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        super.onEntityDeath(event);
        if (getEncounter().checkIfWitches(event.getEntity())) nextState();
    }

    @Override
    public String getStateName() {
        return "LCL E1 Witches";
    }
}
