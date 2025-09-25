package me.depickcator.trablesAdditions.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MainMenuBlockGUI extends TrablesBlockGUI {
    public MainMenuBlockGUI(Block block) {
        super(block, 6, TextUtil.makeText(""));
    }

    @Override
    public boolean interactWithBlock(PlayerData playerData, Block block, PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            new MainMenuGUI(playerData);
            return false;
        }
        return true;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        return false;
    }
}
