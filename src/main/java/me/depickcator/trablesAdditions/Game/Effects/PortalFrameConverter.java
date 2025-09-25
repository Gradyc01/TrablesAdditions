package me.depickcator.trablesAdditions.Game.Effects;

import com.sk89q.worldedit.math.transform.AffineTransform;
import me.depickcator.trablesAdditions.Game.Effects.Interfaces.Floodable;
import me.depickcator.trablesAdditions.Listeners.DimensionalTravel;
import me.depickcator.trablesAdditions.TrablesAdditions;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Random;

public class PortalFrameConverter implements Floodable {
    private final String WORLD_NAME;
    private final Axis axis;
    public PortalFrameConverter(String worldName, Location location) {

        this.WORLD_NAME = worldName;
        Vector v = location.getDirection();
        axis = Math.abs(v.getX()) < Math.abs(v.getZ()) ? Axis.X : Axis.Z;
    }
    @Override
    public Block changeBlock(Block block, Random r, FloodBlocks floodBlocks) {
        Orientable data = (Orientable) Material.NETHER_PORTAL.createBlockData();
        data.setAxis(axis);
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
