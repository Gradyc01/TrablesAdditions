package me.depickcator.trablesAdditions.Game.Items.Crafts.BlackHoleContainer;

import me.depickcator.trablesAdditions.Game.Items.Crafts.PortableWorkbench.PortableWorkbenchSelectionGUI;
import me.depickcator.trablesAdditions.Game.Items.Crafts.PortableWorkbench.Workbench;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BlackHoleContainer extends Craft implements ItemClick {
    private static BlackHoleContainer instance;

    private BlackHoleContainer() {
        super("Black Hole Container", "black_hole_container");
        registerClick(this, this);
    }

    @Override
    protected Recipe initRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, getKey()), result);
        recipe.shape(" B ", "BAB", " B ");
        recipe.setIngredient('A', Material.ENDER_CHEST);
        recipe.setIngredient('B', Material.ENDER_PEARL);
        return recipe;
    }

    @Override
    protected ItemStack initResult() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDYxYjg" +
                "3ZjFhMTA0MGE4YjkyMmNhNTFiZTljMGJjNmQ2ZmM3MWJhNWQ3NDVjNmJmNjU5Y2JkMGQ5YTljZjRmYyJ9fX0=";
        ItemStack item = ItemUtil.buildHead(base64, TextUtil.makeText(getDisplayName(), TextUtil.YELLOW).append(TextUtil.placeText()),
                List.of(TextUtil.makeText("Place to open a mini black hole to be",TextUtil.DARK_PURPLE),
                        TextUtil.makeText("able to transport items out of your realm", TextUtil.DARK_PURPLE)));
        singleStack(item);
        addUnrepairable(item);
        generateUniqueModelString(item);
        return item;
    }

    public static BlackHoleContainer getInstance() {
        if (instance == null) instance = new BlackHoleContainer();
        return instance;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        if (e.getClickedBlock() == null) return false;
        Player player = pD.getPlayer();
        if (RealmController.getController(player.getWorld().getName()) == null) {
            TextUtil.errorMessage(player, "You aren't in a realm!");
            return false;
        }
        new BlackHoleContainerGUI(pD);
        return false;
    }
}
