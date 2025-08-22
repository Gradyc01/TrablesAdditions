package me.depickcator.trablesAdditions.Game.Effects;

import me.depickcator.trablesAdditions.Game.Effects.Interfaces.Floodable;
import me.depickcator.trablesAdditions.Listeners.DimensionalTravel;
import me.depickcator.trablesAdditions.TrablesAdditions;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;
import java.util.Random;

public class PortalFrameConverter implements Floodable {
    private final String WORLD_NAME;
    public PortalFrameConverter(String worldName) {
        this.WORLD_NAME = worldName;
    }
    @Override
    public Block changeBlock(Block block, Random r, FloodBlocks floodBlocks) {
        Orientable data = (Orientable) Material.NETHER_PORTAL.createBlockData();
        data.setAxis(Axis.Z);
        block.setBlockData(data, false);
        block.setMetadata(DimensionalTravel.DIMENSIONAL_TRAVEL_KEY, new FixedMetadataValue(TrablesAdditions.getInstance(), WORLD_NAME));
        return block;
    }

    @Override
    public Map<Material, Integer> getUnFloodables() {
        return Map.of(Material.AIR, 1);
    }

    @Override
    public boolean isUnFloodable(Block b) {
        return !b.getType().equals(Material.REINFORCED_DEEPSLATE);
    }

    @Override
    public double getNewSuccessRate(double oldSuccessRate, Random r) {
        return 1;
    }
}
