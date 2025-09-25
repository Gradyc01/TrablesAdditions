package me.depickcator.trablesAdditions.Game.Items.Uncraftable;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.UI.GrimoireBookGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.WritableBookMeta;

import java.util.List;


public class GrimoireBook extends CustomItem implements ItemClick {
    private static GrimoireBook instance;
    private GrimoireBook() {
        super("Grimoire Book", "grimoire_book");
        registerClick(this, this);
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item =  new ItemStack(Material.WRITTEN_BOOK);
        WritableBookMeta meta = (WritableBookMeta) item.getItemMeta();
        meta.customName(TextUtil.makeText(getDisplayName(), TextUtil.DARK_GRAY).append(TextUtil.rightClickText()));
        meta.lore(List.of(
                TextUtil.makeText("A Collection of all items in the game")
        ));
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        addUnrepairable(item);
        return item;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        new GrimoireBookGUI(pD);
        return false;
    }


    public static GrimoireBook getInstance() {
        if (instance == null) {
            instance = new GrimoireBook();
        }
        return instance;
    }
}
