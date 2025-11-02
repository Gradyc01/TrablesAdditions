package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDeathEvent;

public class LCL_E1_Evoker extends LCL_Encounter1State {
    public LCL_E1_Evoker(LostCityLiberationRealm realm, LCL_Encounter1 encounter) {
        super(realm, encounter);
    }

    @Override
    public LCL_Encounter1State getNextState() {
        return new LCL_E1_Spawners(getRealm(), getEncounter());
    }

    @Override
    public void onSet() {
        super.onSet();
        getEncounter().spawnEvokerWave();
        Audience audience = getEncounter().getAudience();
        audience.sendMessage(
                TextUtil.makeText("The Realm Loosens its Grip", TextUtil.YELLOW, false, true));
        audience.playSound(SoundUtil.makeSound(Sound.ENTITY_ELDER_GUARDIAN_CURSE, 10, 2));
    }

    @Override
    public String getStateName() {
        return "LCL E1 Evoker";
    }
}
