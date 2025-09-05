package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealmBossFight;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerBucketEvent;

import java.util.List;

public abstract class Wither_BossState extends WitherRealmState {
    private final WitherRealmBossFight bossFight;
    private final RealmController controller;
    public Wither_BossState(WitherRealm realm, RealmController controller, WitherRealmBossFight witherRealmBossFight) {
        super(realm);
        this.controller = controller;
        this.bossFight = witherRealmBossFight;
    }

    @Override
    public void onPlayerBucket(PlayerBucketEvent event) {
        event.setCancelled(true);
        TextUtil.errorMessage(event.getPlayer(), "You can't do that right now!");
    }

    public WitherRealmBossFight getBossFight() {
        return bossFight;
    }

    public RealmController getController() {
        return controller;
    }

    public abstract boolean canOpenPanel();

    public abstract void setNextBossState();

    @Override
    public List<Component> getObjectiveName() {
        return List.of(
                TextUtil.makeText("Defeat Krivon")
        );
    }

}
