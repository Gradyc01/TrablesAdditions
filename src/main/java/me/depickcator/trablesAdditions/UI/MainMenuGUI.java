package me.depickcator.trablesAdditions.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.UI.DisplayReward.DisplayRewardGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockMenuGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.UI.LoadoutBuilderUI.OpenLoadoutBuilder;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenuGUI extends TrablesPlayerMenuGUI {
    private final Map<ItemStack, TrablesMenuActionable> actionMap;
    public MainMenuGUI(PlayerData playerData) {
        super(playerData, 3, TextUtil.makeText("Fletching Table", TextUtil.AQUA), true);
        actionMap = new HashMap<>();
        initButtons();
        playerHeadButton(22);
    }

    private void initButtons() {
        placeButton(initExplainerItem(Material.TRIAL_KEY, List.of(), TextUtil.makeText("Purchase Realm Keys", TextUtil.AQUA)),
                13, new PurchaseRealmGUI(playerData));
        placeButton(initExplainerItem(Material.BOOK, List.of(), TextUtil.makeText("Build Loadout", TextUtil.AQUA)),
                11, new OpenLoadoutBuilder());
        placeButton(initExplainerItem(Material.CHEST, List.of(), TextUtil.makeText("Grab Loot", TextUtil.AQUA)),
                15, new DisplayRewardGUI(playerData));
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
