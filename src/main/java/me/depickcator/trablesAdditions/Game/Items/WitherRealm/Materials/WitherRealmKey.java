package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.List;

public class WitherRealmKey extends CustomItem {
    private static WitherRealmKey instance;
    private WitherRealmKey() {
        super("Wither Realm Key", "wither_realm_key");
    }

    @Override
    protected ItemStack initResult() {;
        ItemStack item = new ItemStack(Material.TRIAL_KEY);
        Repairable meta = (Repairable) item.getItemMeta();
        meta.lore(List.of(
                TextUtil.makeText("Forged in the heart of the realm ", TextUtil.DARK_PURPLE),
                TextUtil.makeText("Unlocks Krivon's deepest treasures", TextUtil.DARK_PURPLE)
        ));
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.YELLOW));
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        addUnrepairable(item);
        return item;
    }

    public static WitherRealmKey getInstance() {
        if (instance == null) {
            instance = new WitherRealmKey();
        }
        return instance;
    }
}
