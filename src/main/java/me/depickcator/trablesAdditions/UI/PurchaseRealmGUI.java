package me.depickcator.trablesAdditions.UI;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.EntranceKey;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.WitherRealmEntranceKey;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuActionable;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesPlayerMenuGUI;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.guieffect.qual.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseRealmGUI extends TrablesPlayerMenuGUI implements TrablesMenuActionable {
    private final Map<ItemStack, Pair<EntranceKey, ItemStack>> keys;
    public PurchaseRealmGUI(PlayerData playerData) {
        super(playerData, 3, TextUtil.makeText("Purchase Realm Keys", TextUtil.AQUA), true, false);
        keys = new HashMap<>();
        inventory.setItem(21, goBackItem());
        initKeys();
    }

    @Override
    public boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event) {
        open(player);
        return true;
    }

    @Override
    public boolean interactWithGUIButtons(PlayerData playerData, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return false;
        if (item.equals(goBackItem())) {
            new MainMenuGUI(playerData);
        }
        if (keys.containsKey(item)) {
            Pair<EntranceKey, ItemStack> pair = keys.get(item);
            if (pair.getKey().canPurchase(playerData)) {
                SoundUtil.playHighPitchPling(player);
                PlayerUtil.giveItem(player, pair.getRight());
                new PurchaseRealmGUI(playerData).open(player);
            }
        }
        return false;
    }

    private void initKeys() {
        initKey(WitherRealmEntranceKey.getInstance(), WitherRealmEntranceKey.getInstance(), 13);
    }

    private void initKey(EntranceKey entranceKey, CustomItem item, int index) {
        ItemStack icon = item.getResult();
        icon.setAmount(1);
        ItemMeta meta = icon.getItemMeta();
        meta.customName(TextUtil.makeText(item.getDisplayName(), TextUtil.GOLD));
        List<Component> lore = new ArrayList<>(entranceKey.description());
        lore.add(TextUtil.makeText(""));
        lore.add(TextUtil.makeText("Cost:", TextUtil.GOLD));
        lore.addAll(entranceKey.purchaseRequirements());
        lore.add(TextUtil.makeText(""));
        lore.add(TextUtil.makeText("Recommended Items:", TextUtil.GOLD));
        lore.addAll(entranceKey.requiredItems());
        meta.lore(lore);
        icon.setItemMeta(meta);
        inventory.setItem(index, icon);
        keys.put(icon, Pair.of(entranceKey, item.getResult()));
    }
}
