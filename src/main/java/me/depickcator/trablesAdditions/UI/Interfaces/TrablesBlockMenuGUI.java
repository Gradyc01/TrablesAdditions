package me.depickcator.trablesAdditions.UI.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class TrablesBlockMenuGUI extends TrablesMenuGUI implements BlockUI {
    private final String KEY;
    private final Block block;
    public TrablesBlockMenuGUI(Block block, int GUILines, Component name, boolean setBackground) {
        super(null, GUILines, name, setBackground);
        this.KEY = "";
        this.block = block;
        TrablesBlockGUI.registerGUI(this);
    }

    @Override
    public Block getBlock() {
        return block;
    }
}
