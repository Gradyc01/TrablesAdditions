package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class LCL_E1_End extends LCL_Encounter1State{
    public LCL_E1_End(LostCityLiberationRealm realm, LCL_Encounter1 encounter) {
        super(realm, encounter);
    }

    @Override
    public LCL_Encounter1State getNextState() {
        return null;
    }

    private void noMoreLivingEntities(Location loc) {
        TextUtil.debugText(loc.getWorld().getNearbyLivingEntities(loc, 200, 200, 200).size() + "");
        for (LivingEntity entity : loc.getWorld().getNearbyLivingEntities(loc, 200, 200, 200)) {
            if (entity instanceof Player) continue;
            entity.remove();
        }
    }

    @Override
    public void onSet() {
        super.onSet();
        Audience audience = getEncounter().getAudience();
        audience.sendMessage(TextUtil.makeText("The ground begins to split...", TextUtil.YELLOW));
        audience.playSound(SoundUtil.makeSound(Sound.ENTITY_WARDEN_EMERGE, 10, 2));
        noMoreLivingEntities(getEncounter().getPlayers().getFirst().getLocation());
        getEncounter().stopLoop();
    }

    @Override
    public String getStateName() {
        return "Encounter 1: End";
    }
}
