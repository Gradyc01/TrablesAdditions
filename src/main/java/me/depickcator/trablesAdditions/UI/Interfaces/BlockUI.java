package me.depickcator.trablesAdditions.UI.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public interface BlockUI {
    boolean interactWithBlock(PlayerData playerData, Block block, PlayerInteractEvent event);
    Block getBlock();

}
