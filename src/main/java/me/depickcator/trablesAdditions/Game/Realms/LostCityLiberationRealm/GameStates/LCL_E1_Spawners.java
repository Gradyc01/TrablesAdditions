package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Sound;

public class LCL_E1_Spawners extends LCL_Encounter1State {
    public LCL_E1_Spawners(LostCityLiberationRealm realm, LCL_Encounter1 encounter) {
        super(realm, encounter);
    }

    @Override
    public LCL_Encounter1State getNextState() {
        return new LCL_E1_Skirmish(getRealm(), getEncounter());
    }

    @Override
    public void onSet() {
        super.onSet();
        Audience audience = getEncounter().getAudience();
        audience.sendMessage(TextUtil.makeText("The Spawners have been activated", TextUtil.YELLOW));
        audience.playSound(SoundUtil.makeSound(Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, 10, 1));
    }

    @Override
    public String getStateName() {
        return "LCL E1 Spawners";
    }
}
