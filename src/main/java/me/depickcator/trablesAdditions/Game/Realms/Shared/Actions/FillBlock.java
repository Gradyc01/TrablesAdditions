package me.depickcator.trablesAdditions.Game.Realms.Shared.Actions;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmActions;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Listeners.DimensionalTravel;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.IOException;
import java.util.List;

public class FillBlock extends RealmActions {
    private final Material material;
    private final String key;
    public FillBlock(String meshName, RealmController controller, Material material, String key) {
        super(meshName, controller);
        this.material = material;
        this.key = key;
    }

    public FillBlock(String meshName, RealmController controller, Material material) {
        this(meshName, controller, material, "");
    }

    public boolean start() {
        try {
            LocationMesh mesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            for (Pair<Location, Integer> pair : mesh.getAllLocationsWeighted()) {
                Block block = pair.getLeft().getBlock();
                BlockData data =  material.createBlockData();
                if (data instanceof Orientable orientable) {
                    orientable.setAxis(pair.getRight() % 2 == 0 ? Axis.Z : Axis.X);
                    block.setBlockData(orientable, false);
                }
                block.setBlockData(data, false);
                if (!key.isEmpty()) {
                    block.setMetadata(DimensionalTravel.DIMENSIONAL_TRAVEL_KEY,
                            new FixedMetadataValue(TrablesAdditions.getInstance(), key));
                }
            }
            return true;
        } catch (IOException e) {
            TextUtil.debugText("Fill Block", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }
}
