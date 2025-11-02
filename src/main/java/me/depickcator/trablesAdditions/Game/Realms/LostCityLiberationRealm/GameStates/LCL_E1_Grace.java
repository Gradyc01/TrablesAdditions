package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

public class LCL_E1_Grace extends LCL_Encounter1State {
    public LCL_E1_Grace(LostCityLiberationRealm realm, LCL_Encounter1 encounter) {
        super(realm, encounter);
    }

    @Override
    public LCL_Encounter1State getNextState() {
        return new LCL_E1_Witches(getRealm(), getEncounter());
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
        audience.sendMessage(TextUtil.makeText("There seems to be a brief moment of peace", TextUtil.YELLOW));
        TrablesAdditions.getInstance().getServer().getScheduler().runTaskLater(TrablesAdditions.getInstance(), this::nextState, 20 * 20);
        noMoreLivingEntities(getEncounter().getPlayers().getFirst().getLocation());
    }

    @Override
    public String getStateName() {
        return "LCL E1 Grace";
    }
}
