package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealmLoot;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealm_Tier1Loot;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealm_Tier2Loot;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI.WitherRealm_LootGUI;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.Random;

public class WitherRealm_FillLoot extends WitherRealmActions {
    private final WitherRealm realm;
    /*Material must contain a material that has an inventory*/
    public WitherRealm_FillLoot(String meshName, RealmController controller, WitherRealm realm) {
        super(meshName, controller);
        this.realm = realm;
    }

    public boolean start() {
        try {
            LocationMesh room1Mesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            Random random = new Random();
            WitherRealmLoot lootPool = new WitherRealm_Tier1Loot();
            WitherRealmLoot lootPoolTier2 = new WitherRealm_Tier2Loot();

            for (Pair<Location, Integer> spawnLoc : room1Mesh.getAllLocationsWeighted()) {
                Block block = spawnLoc.getLeft().getBlock();
                Pair<Material, WitherRealmLoot> chestType = switch (spawnLoc.getRight()) {
                    default -> Pair.of(Material.CHEST, lootPool);
                    case 2 -> Pair.of(Material.ENDER_CHEST, lootPoolTier2);
                };
                block.setType(chestType.getLeft());
                realm.addLootChestGUI(new WitherRealm_LootGUI(block, chestType.getRight(), random));
            }
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Wither Realm Fill Loot", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
