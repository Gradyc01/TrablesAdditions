package me.depickcator.trablesAdditions.Game.Effects;

import me.depickcator.trablesAdditions.Game.Effects.Interfaces.Floodable;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FloodBlocks {
    private final Set<Pair<Location, Double>> floodLocations;
    private final Location startingPoint;
    private final double successRate;
    private final Floodable floodable;
    private final int totalWeight;

    public FloodBlocks(Location startingPoint, double successRate, Floodable floodable) {
        floodLocations = new HashSet<>();
        this.startingPoint = startingPoint;
        this.successRate = successRate;
        this.floodable = floodable;
        totalWeight = calculateWeight();
    }

    public void start(Random r) {
        floodBlock(startingPoint, successRate, r);
    }

    public void flood(Random r) {
        TextUtil.debugText("RealmAnimation","Flood Size " + floodLocations.size());
        for (Pair<Location, Double> floodLocation : new HashSet<>(floodLocations)) {
            floodBlock(floodLocation.getLeft(), floodLocation.getRight(), r);
            floodLocations.remove(floodLocation);
        }
    }

    public void autoFlood(Random r) {
        floodBlock(startingPoint, successRate, r);
        while (!floodLocations.isEmpty()) {
            flood(r);
        }
    }

    public Material chooseNextMaterial(Random r) {
        int nextMaterial = r.nextInt(0, totalWeight);
        int indexWeight = 0;
//        String debugString = "Obtaining Process: ";
        for (Map.Entry<Material, Integer> flood : floodable.getUnFloodables().entrySet()) {
            indexWeight += flood.getValue();
//            debugString += indexWeight + " --> ";
            if (indexWeight > nextMaterial) {
//                TextUtil.debugText(debugString + "Material is " + flood.getKey().name());
                return flood.getKey();
            }
        }
        return floodable.getUnFloodables().entrySet().iterator().next().getKey();
    }

    private void floodBlock(Location loc, double successRate, Random r) {
        if (r.nextDouble() > successRate) return;
        Block block = loc.getWorld().getBlockAt(loc);
//        if (block.getType() == Material.END_STONE
//                || block.getType() == Material.OBSIDIAN
//                || block.getType() == Material.AIR) return;
        if (floodable.isUnFloodable(block)) return;
        floodable.changeBlock(block, r, this);
//        Material type = r.nextDouble() <= 0.95 ? Material.END_STONE : Material.OBSIDIAN;
//        block.setType(type);

//        block.setBlockData(b.getBlockData());
//        block.setMetadata("UNBREAKABLE", new FixedMetadataValue(Ascension.getInstance(), true));

//        double newSuccessRate = Double.max(0, successRate - r.nextDouble(0.10, 0.25));
        double newSuccessRate = Double.max(0, floodable.getNewSuccessRate(successRate, r));
        floodLocations.add(Pair.of(loc.clone().add(1, 0, 1), newSuccessRate));
        floodLocations.add(Pair.of(loc.clone().add(1, 0, 0), newSuccessRate));
        floodLocations.add(Pair.of(loc.clone().add(1, 0, -1), newSuccessRate));

        floodLocations.add(Pair.of(loc.clone().add(0, 0, 1), newSuccessRate));
        floodLocations.add(Pair.of(loc.clone().add(0, 0, -1), newSuccessRate));

        floodLocations.add(Pair.of(loc.clone().add(-1, 0, 1), newSuccessRate));
        floodLocations.add(Pair.of(loc.clone().add(-1, 0, 0), newSuccessRate));
        floodLocations.add(Pair.of(loc.clone().add(-1, 0, -1), newSuccessRate));

        floodLocations.add(Pair.of(loc.clone().add(0, -1, 0), newSuccessRate));
        floodLocations.add(Pair.of(loc.clone().add(0, 1, 0), newSuccessRate));
    }

    private int calculateWeight() {
        int totalWeight = 0;
        for (Map.Entry<Material, Integer> flood : floodable.getUnFloodables().entrySet()) {
            totalWeight += flood.getValue();
        }
        return totalWeight;
    }
}
