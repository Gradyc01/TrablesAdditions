package me.depickcator.trablesAdditions.Game.Realms.WitherRealm;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmActions;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action.WitherRealm_LoadRoom;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates.Wither_BossState;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmWither;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI.WitherRealmBossControlPanelGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class WitherRealmBossFight {
    private WitherRealmWither wither;
    private final List<WitherRealmBossControlPanelGUI> panels;
    private final List<List<RealmActions>> mobWaves;
    private final WitherRealm realm;
    private final RealmController controller;
    public WitherRealmBossFight(WitherRealm realm, RealmController controller) {
        this.wither = null;
        this.controller = controller;
        this.realm = realm;
        this.panels = new ArrayList<>();
        this.mobWaves = new ArrayList<>();
        addMobWaves();
//        addMobWaves();
    }

    public void setWither(WitherRealmWither wither) {
        this.wither = wither;
    }

    public WitherRealmWither getWither() {
        return wither;
    }

    public void bossDefeated() {
        controller.bossDefeated();
    }

    public void addPanel(WitherRealmBossControlPanelGUI panel) {
        panels.add(panel);
        TextUtil.debugText("Panel", "Adding panel " + panels.size());
    }

    public void spawnMobWave() {
        if (mobWaves.isEmpty()) return;
        List<RealmActions> wave = mobWaves.removeFirst();
        for (RealmActions action : wave) {
            action.start();
        }
//        mobWaves.add(wave);
    }

    public void removePanel(WitherRealmBossControlPanelGUI panel) {
        panels.remove(panel);
        addMobWaves();
        TextUtil.debugText("Panel", "Removing panel " + (panels.size()));
        if (panels.isEmpty()) {
            wither.startPhaseTwo();
            if (realm.getRealmState() instanceof Wither_BossState bossState) {
                bossState.setNextBossState();
            } else {
                TextUtil.debugText("Wither Realm Boss Fight", "Boss is in the wrong state: " + realm.getRealmState().getStateName());
                controller.stopRealm();
            }
        }
    }

    private void addMobWaves() {
        RealmActions l1 = new WitherRealm_LoadRoom("room_boss_l1", controller);
        RealmActions l2 = new WitherRealm_LoadRoom("room_boss_l2", controller);
        RealmActions l3 = new WitherRealm_LoadRoom("room_boss_l3", controller);
        RealmActions r1 = new WitherRealm_LoadRoom("room_boss_r1", controller);
        RealmActions r2 = new WitherRealm_LoadRoom("room_boss_r2", controller);
        RealmActions r3 = new WitherRealm_LoadRoom("room_boss_r3", controller);
        RealmActions m1 = new WitherRealm_LoadRoom("room_boss_m1", controller);
        RealmActions m2 = new WitherRealm_LoadRoom("room_boss_m2", controller);
        RealmActions m3 = new WitherRealm_LoadRoom("room_boss_m3", controller);
        mobWaves.addAll(new ArrayList<>(List.of(
                List.of(m1, m2),
                List.of(l1, r2),
                List.of(r1, l2),
                List.of(m3, m2),
                List.of(l3, r3))));
    }
}
