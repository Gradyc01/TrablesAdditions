package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.List;

public class KrivonHandle extends CustomItem {
    private static KrivonHandle instance;
    private KrivonHandle() {
        super("Krivon's Handle", "krivon_handle");
    }

    @Override
    protected ItemStack initResult() {;
        ItemStack item = new ItemStack(Material.STICK);
        Repairable meta = (Repairable) item.getItemMeta();
        meta.lore(List.of(
                TextUtil.makeText("Used in the process of forging", TextUtil.DARK_PURPLE),
                TextUtil.makeText("Krivon's greatest weapons", TextUtil.DARK_PURPLE)
        ));
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.DARK_RED));
        meta.setEnchantmentGlintOverride(true);
        meta.setRepairCost(999);
        item.setItemMeta(meta);
        return item;
    }

    public static KrivonHandle getInstance() {
        if (instance == null) {
            instance = new KrivonHandle();
        }
        return instance;
    }
}
