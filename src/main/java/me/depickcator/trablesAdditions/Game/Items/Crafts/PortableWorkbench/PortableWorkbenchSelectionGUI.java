package me.depickcator.trablesAdditions.Game.Items.Crafts.PortableWorkbench;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortableWorkbenchSelectionGUI extends TrablesPlayerMenuGUI {
    private final ItemStack item;
    private final Map<ItemStack, Pair<Workbench, Boolean>> menus;
    public PortableWorkbenchSelectionGUI(PlayerData pD, ItemStack item) {
        super(pD, 3, TextUtil.makeText("Select a workbench", TextUtil.AQUA), true);
        this.item = item;
        this.menus = new HashMap<>();
        initWorkbenches();
    }

    private void initWorkbenches() {
        initWorkbench(11, PortableWorkbench.ANVIL);
        initWorkbench(13, PortableWorkbench.CRAFTING_KEY);
        initWorkbench(15, PortableWorkbench.ENCHANTING_KEY);
    }

    private void initWorkbench(int index, Workbench workbench) {
        Pair<Workbench, Boolean> menu =  workbench.getKeyOnItem(item);
        ItemStack item = makeItem(workbench, menu.getRight());
        menus.put(item, menu);
        inventory.setItem(index, item);
    }

    private ItemStack makeItem(Workbench workbench, boolean unlocked) {
//        ItemStack item =
        List<Component> lore =  new ArrayList<>();
        lore.add(unlocked ? TextUtil.makeText("UNLOCKED", TextUtil.DARK_GREEN, true, false) :
                TextUtil.makeText("NOT UNLOCKED", TextUtil.RED, true, false));
        lore.add(unlocked ? TextUtil.makeText("Left Click to use " + workbench.getName(), TextUtil.YELLOW) :
                TextUtil.makeText("Left Click to deposit a " + workbench.getName(), TextUtil.RED));
        lore.add(unlocked ? TextUtil.makeText("Right Click to extract workbench", TextUtil.RED) : TextUtil.makeText("into the portable workbench", TextUtil.RED));
        return initExplainerItem(workbench.getMaterial(), lore, TextUtil.makeText(workbench.getName(),
                unlocked ? TextUtil.DARK_GREEN : TextUtil.RED));
    }




    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (menus.containsKey(item)) {
            Pair<Workbench, Boolean> menu = menus.get(item);
            Workbench workbench = menu.getLeft();
            if (menu.getRight() && event.isLeftClick()) {
                event.setCancelled(true);
                inventory.close();
                player.playSound(SoundUtil.makeSound(Sound.ENTITY_HORSE_SADDLE, 1.0f, 1.0f));
                workbench.getType().create(player, TextUtil.makeText("Workbench: ", TextUtil.DARK_GREEN)
                        .append(TextUtil.makeText(TextUtil.getItemNameString(item), TextUtil.YELLOW))).open();
            } else {
                if (event.isLeftClick()) {
                    if (player.getInventory().contains(workbench.getMaterial())) {
                        player.getInventory().remove(workbench.getMaterial());
                        SoundUtil.playHighPitchPling(player);
                        workbench.setKeyOnItem(this.item, true);
                        new PortableWorkbenchSelectionGUI(playerData, this.item);
                    } else {
                        TextUtil.errorMessage(player, "Can not unlock as the required material is not there");
                    }
                } else if (event.isRightClick() && menu.getRight()) {
                    SoundUtil.playHighPitchPling(player);
                    player.sendMessage(TextUtil.makeText("Successfully extracted " + workbench.getName() +
                            " from the portable workbench", TextUtil.DARK_GREEN));
                    PlayerUtil.giveItem(player, new ItemStack(workbench.getMaterial()));
                    workbench.setKeyOnItem(this.item, false);
                    new PortableWorkbenchSelectionGUI(playerData, this.item);
                }
            }
        }
        return false;
    }
}
