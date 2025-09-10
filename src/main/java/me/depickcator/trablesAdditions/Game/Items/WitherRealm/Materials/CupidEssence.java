package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.List;

public class CupidEssence extends CustomItem {
    private static CupidEssence instance;
    private CupidEssence() {
        super("Cupid's Essence", "cupid_essence");
    }

    @Override
    protected ItemStack initResult() {;
        ItemStack item = new ItemStack(Material.BLAZE_POWDER);
        Repairable meta = (Repairable) item.getItemMeta();
        meta.lore(List.of(
                TextUtil.makeText("A stolen artifact for which Krivon", TextUtil.DARK_PURPLE),
                TextUtil.makeText("bestowed to his greatest archers", TextUtil.DARK_PURPLE)
        ));
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.RED));
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        addUnrepairable(item);
        return item;
    }

    public static CupidEssence getInstance() {
        if (instance == null) {
            instance = new CupidEssence();
        }
        return instance;
    }
}
