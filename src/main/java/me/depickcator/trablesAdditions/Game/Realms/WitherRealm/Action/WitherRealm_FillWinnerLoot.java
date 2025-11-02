package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmActions;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealmLoot;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealm_Tier3Loot;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI.WitherRealm_PrizeBlockGUI;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.Random;

public class WitherRealm_FillWinnerLoot extends RealmActions {
    private final WitherRealm realm;
    /*Material must contain a material that has an inventory*/
    public WitherRealm_FillWinnerLoot(String meshName, RealmController controller, WitherRealm realm) {
        super(meshName, controller);
        this.realm = realm;
    }

    public boolean start() {
        try {
            LocationMesh room1Mesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            Random random = new Random();
            WitherRealmLoot lootPool = new WitherRealm_Tier3Loot();
            for (Pair<Location, Integer> spawnLoc : room1Mesh.getAllLocationsWeighted()) {
                Block block = spawnLoc.getLeft().getBlock();
                block.setType(Material.TRIAL_SPAWNER);
//                block.setType(random.nextInt(0, 2) == 0 ? Material.ENDER_CHEST : Material.CHEST);
                realm.addLootChestGUI(getMeshName(), new WitherRealm_PrizeBlockGUI(block, lootPool, random, controller.getRealmPlayers().getPlayers()));
            }
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Wither Realm Fill Winner Loot", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
