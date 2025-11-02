package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters;

import me.depickcator.trablesAdditions.Game.Effects.LCLTrueSight;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.CleanseDisplay;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class LCL_Encounter {
    protected final LostCityLiberationRealm realm;
    private final String name;
    private final Map<Player, LCLTrueSight> trueSightMap;
    protected final RealmController controller;
    private final Set<CleanseDisplay> cleanseDisplays;
    private boolean runLoop;

    public LCL_Encounter(LostCityLiberationRealm realm, String name, RealmController controller) {
        this.realm = realm;
        this.name = name;
        this.controller = controller;
        this.trueSightMap = new HashMap<>();
        this.cleanseDisplays = new HashSet<>();
        runLoop = true;
    }
    public void onLoop(){
            if (runLoop) uponLoop();
    };

    protected abstract void uponLoop();

    public void set() {
        realm.setEncounter(this);
    }

    public void giveTrueSight(Player player) {
        LCLTrueSight sight = new LCLTrueSight(player, controller);
        trueSightMap.put(player, sight);
        sight.start();
    }

    public void removeTrueSight(Player player) {
        trueSightMap.remove(player);
    }

    public void cleanseTrueSight(Player player) {
//        PlayerData playerData = PlayerUtil.getPlayerData(player);
        if (trueSightMap.containsKey(player)) {
            trueSightMap.remove(player).cleanse();
        }
    }

    public void addCleanseDisplay(CleanseDisplay cleanseDisplay) {
        cleanseDisplays.add(cleanseDisplay);
    }

    public void removeCleanseDisplay(CleanseDisplay cleanseDisplay) {
        cleanseDisplay.remove(Entity.RemovalReason.DISCARDED);
        cleanseDisplays.remove(cleanseDisplay);
    }

    public void clearCleanseDisplays() {
        for (CleanseDisplay cleanseDisplay : cleanseDisplays) {
            cleanseDisplay.remove(Entity.RemovalReason.DISCARDED);
        }
        cleanseDisplays.clear();
    }

    public RealmStates getRealmStates() {
        return realm.getRealmState();
    }

    public void startLoop() {
        runLoop = true;
    }

    public void stopLoop() {
        runLoop = false;
    }

    public String getName() {
        return name;
    }
}
