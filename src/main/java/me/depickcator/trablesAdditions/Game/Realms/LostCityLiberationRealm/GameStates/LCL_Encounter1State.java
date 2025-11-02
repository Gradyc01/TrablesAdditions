package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public abstract class LCL_Encounter1State extends LostCityLibState {
    private final LCL_Encounter1 encounter;

    public LCL_Encounter1State(LostCityLiberationRealm realm, LCL_Encounter1 encounter) {
        super(realm);
        this.encounter = encounter;
    }

    @Override
    public boolean onPlayerInteract(PlayerInteractEvent event, RealmController controller) {
        if (!super.onPlayerInteract(event, controller)) return false;
        if (event.getClickedBlock() == null) return true;
        if (encounter.attemptTriggerCleanseDoor(event.getClickedBlock(), PlayerUtil.getPlayerData(event.getPlayer()))) {
            event.setCancelled(true);
            return false;
        }
        if (event.getAction().isRightClick() && event.getClickedBlock().getType().name().toLowerCase().contains("door")) {
            event.setCancelled(true);
            return false;
        }
        return true;
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, RealmController controller) {
        super.onPlayerDeath(event, controller);
        encounter.removeTrueSight(event.getPlayer());
    }

    @Override
    public List<Component> getObjectiveName() {
        return List.of(
                TextUtil.makeText("Unlock the Gate", TextUtil.YELLOW),
                TextUtil.makeText("To The Lost City", TextUtil.YELLOW)
        );
    }

    public abstract LCL_Encounter1State getNextState();

    public void nextState() {
        getRealm().setRealmState(getNextState());
    }

    public LCL_Encounter1 getEncounter() {
        return encounter;
    }
}
