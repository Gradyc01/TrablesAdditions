package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public abstract class LCLSymbol extends CustomItem implements ItemClick {
    private static LCLSymbol instance;
    private final NamespacedKey key = new NamespacedKey(TrablesAdditions.getInstance(), "SymbolWorld");

    public LCLSymbol(String displayName, String key) {
        super(displayName, "lcl_" + key);
        registerClick(this, this);
    }


    @Override
    protected ItemStack initResult() {
        ItemStack item = ItemUtil.buildHead(getBase64(), TextUtil.makeText(getDisplayName(), getColor()),
                List.of(TextUtil.makeText("",TextUtil.DARK_PURPLE)));
        addUnrepairable(item);
        generateUniqueModelString(item);
        singleStack(item);
        return item;
    }

    protected abstract String getBase64();
    protected abstract TextColor getColor();

    @Override
    public ItemStack getResult() {
        ItemStack item = super.getResult();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, UUID.randomUUID().toString());
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        return false;
    }
}
