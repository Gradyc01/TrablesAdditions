package me.depickcator.trablesAdditions.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenuGUI extends TrablesMenuGUI {
    private final Map<ItemStack, TrablesMenuActionable> actionMap;
    public MainMenuGUI(PlayerData playerData) {
        super(playerData, 6, TextUtil.makeText("Main Menu", TextUtil.AQUA), true);
        actionMap = new HashMap<>();
        initButtons();
    }

    private void initButtons() {
        placeButton(initExplainerItem(Material.WITHER_SKELETON_SKULL,
                List.of(TextUtil.makeText("[Replace With Text]", TextUtil.DARK_PURPLE)),
                TextUtil.makeText("Test Realm", TextUtil.AQUA)), 31, new WitherRealm(player.getLocation()));
    }

    private void placeButton(ItemStack item, int index, TrablesMenuActionable actionable) {
        inventory.setItem(index, item);
        actionMap.put(item, actionable);
    }


    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (actionMap.containsKey(item)) {
            TrablesMenuActionable actionable = actionMap.get(item);
            if (actionable.runAction(playerData, this, event)) {
                SoundUtil.playHighPitchPling(playerData.getPlayer());
            } else {
                SoundUtil.playErrorSoundEffect(playerData.getPlayer());
            }
        }
        return false;
    }
}
