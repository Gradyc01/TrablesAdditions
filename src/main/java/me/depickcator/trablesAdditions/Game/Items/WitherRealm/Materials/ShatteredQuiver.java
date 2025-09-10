package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ShatteredQuiver extends CustomItem {
    private static ShatteredQuiver instance;
    private ShatteredQuiver() {
        super("Shattered Quiver", "shattered_quiver");
    }

    @Override
    protected ItemStack initResult() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhiY" +
                "mFhMGU0MjBiNjNlNmYyODUwNDZmNGExN2FkNjgyNGY5YzU5ZGM4M2M5ODQ3ZGU0NjU3MGJiZGY5ZmQ0OCJ9fX0=";
        ItemStack item = ItemUtil.buildHead(base64, TextUtil.makeText(getDisplayName(), TextUtil.YELLOW), List.of(
                        TextUtil.makeText("The remnants of those who tried", TextUtil.DARK_PURPLE),
                        TextUtil.makeText("to conquer this place before ", TextUtil.DARK_PURPLE)));
        ItemMeta meta =  item.getItemMeta();
        meta.setMaxStackSize(1);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    public static ShatteredQuiver getInstance() {
        if (instance == null) {
            instance = new ShatteredQuiver();
        }
        return instance;
    }
}
