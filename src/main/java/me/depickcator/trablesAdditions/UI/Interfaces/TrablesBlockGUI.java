package me.depickcator.trablesAdditions.UI.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class TrablesBlockGUI extends TrablesGUI implements BlockUI {
    private final static Map<Block, BlockUI> GUIMAP = new HashMap<>();
    private final Block block;
    public TrablesBlockGUI(Block block, int GUILines, Component name) {
        super(null, GUILines, name);
        this.block = block;
        TrablesBlockGUI.registerGUI(this);
    }

    @Override
    public Block getBlock() {
        return block;
    }

    public static void registerGUI(BlockUI gui) {
        GUIMAP.put(gui.getBlock(), gui);
    }

    public static BlockUI findGUI(Block block) {
        return GUIMAP.get(block);
    }

    public static void removeGUI(BlockUI gui) {
        TextUtil.debugText("Removing GUI: " + gui.getBlock().getType().name());
        GUIMAP.remove(gui.getBlock());
    }

}
