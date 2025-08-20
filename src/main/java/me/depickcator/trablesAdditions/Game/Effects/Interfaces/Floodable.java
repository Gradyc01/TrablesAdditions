package me.depickcator.trablesAdditions.Game.Effects.Interfaces;

import me.depickcator.trablesAdditions.Game.Effects.FloodBlocks;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public interface Floodable {
    /*Changes Block: block to be its new form and returns it*/
    Block changeBlock(Block block, Random r, FloodBlocks floodBlocks);
    /*Gets a set of materials with its associated weight that will stop the flooding of the blocks
    * The weight could be used to represent how often it changes into that type of block*/
    Map<Material, Integer> getUnFloodables();
    /*Returns true if the Block b is UnFloodable*/
    boolean isUnFloodable(Block b);
    /*Returns the new successRate based on the oldSuccessRate
    Return value ideally should be between 0 - 1*/
    double getNewSuccessRate(double oldSuccessRate, Random r);
}
