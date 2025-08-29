package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealm_Tier1Loot;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Loot.WitherRealm_Tier2Loot;
import me.depickcator.trablesAdditions.LootTables.Interfaces.CustomChestLoot;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;

import java.io.IOException;
import java.util.Random;

public class WitherRealm_FillLoot extends WitherRealmActions {
    private final Material chestType;
    /*Material must contain a material that has an inventory*/
    public WitherRealm_FillLoot(String meshName, RealmController controller, Material chestType) {
        super(meshName, controller);
        this.chestType = chestType;
    }

    public boolean start() {
        try {
            LocationMesh room1Mesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            Random random = new Random();
            CustomChestLoot lootPool = new WitherRealm_Tier1Loot();
            CustomChestLoot lootPoolTier2 = new WitherRealm_Tier2Loot();

            for (Pair<Location, Integer> spawnLoc : room1Mesh.getAllLocationsWeighted()) {
                Block block = spawnLoc.getLeft().getBlock();
                Material chestType = spawnLoc.getRight() == 2 ? Material.TRAPPED_CHEST : Material.CHEST;
                block.setType(chestType);
                Container container = (Container) block.getState();
                switch (spawnLoc.getRight()) {
                    case 2 -> {
                        lootPoolTier2.populateLoot(container.getInventory(), random, 1.0);
                    }
                    default -> lootPool.populateLoot(container.getInventory(), random, 1.0);
                }
            }
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Wither Realm Fill Loot", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
