package me.depickcator.trablesAdditions.Game.Effects;

import me.depickcator.trablesAdditions.Game.Effects.Interfaces.Floodable;
import me.depickcator.trablesAdditions.Listeners.DimensionalTravel;
import me.depickcator.trablesAdditions.TrablesAdditions;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Map;
import java.util.Random;

public class PortalFrameRemover implements Floodable {
    public PortalFrameRemover() {
    }
    @Override
    public Block changeBlock(Block block, Random r, FloodBlocks floodBlocks) {
        block.setBlockData(Material.AIR.createBlockData(), true);
        block.removeMetadata(DimensionalTravel.DIMENSIONAL_TRAVEL_KEY, TrablesAdditions.getInstance());
        return block;
    }

    @Override
    public Map<Material, Integer> getUnFloodables() {
        return Map.of(Material.AIR, 1);
    }

    @Override
    public boolean isUnFloodable(Block b) {
        return !b.getType().equals(Material.NETHER_PORTAL);
    }

    @Override
    public double getNewSuccessRate(double oldSuccessRate, Random r) {
        return 1;
    }
}
