package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealmBossFight;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class Wither_BossState_Phase1 extends Wither_BossState {
    public Wither_BossState_Phase1(WitherRealm realm, RealmController controller, WitherRealmBossFight bossFight) {
        super(realm, controller, bossFight);
    }

    @Override
    public void onSet() {
        super.onSet();
        getBossFight().getWither().startPhaseOne();
    }

    @Override
    public boolean canOpenPanel() {
        return true;
    }

    @Override
    public void setNextBossState() {
        getRealm().setRealmState(new Wither_BossState_Phase2(getRealm(), getController(), getBossFight()));
    }

    @Override
    public List<Component> getObjectiveName() {
        return List.of(
                TextUtil.makeText("Disable Shields")
        );
    }

    @Override
    public String getStateName() {
        return "Boss Phase 1";
    }
}
