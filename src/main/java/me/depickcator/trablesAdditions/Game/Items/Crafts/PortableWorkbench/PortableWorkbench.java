package me.depickcator.trablesAdditions.Game.Items.Crafts.PortableWorkbench;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.CupidEssence;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
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

public class PortableWorkbench extends Craft implements ItemClick {
    private static PortableWorkbench instance;
//    public static NamespacedKey CRAFTING_KEY = new NamespacedKey(TrablesAdditions.getInstance(), "crafting_table");
//    public static NamespacedKey ANVIL = new NamespacedKey(TrablesAdditions.getInstance(), "anvil");
//    public static NamespacedKey ENCHANTING_KEY = new NamespacedKey(TrablesAdditions.getInstance(), "enchanting_table");
    public static Workbench CRAFTING_KEY = new Workbench(MenuType.CRAFTING, Material.CRAFTING_TABLE, "Crafting Table");
    public static Workbench ANVIL = new Workbench(MenuType.ANVIL, Material.ANVIL, "Anvil");
    public static Workbench ENCHANTING_KEY = new Workbench(MenuType.ENCHANTMENT, Material.ENCHANTING_TABLE, "Enchanting Table");

    private PortableWorkbench() {
        super("Portable Workbench", "portable_workbench");
        registerItem(this, this);
    }

    @Override
    protected Recipe initRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, getKey()), result);
        recipe.shape("BCB", "BAB", "BBB");
        recipe.setIngredient('A', CupidEssence.getInstance().getResult());
        recipe.setIngredient('B', Material.IRON_INGOT);
        recipe.setIngredient('C', Material.REDSTONE_TORCH);
        return recipe;
    }

    @Override
    protected ItemStack initResult() {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg5ZjF" +
                "lODc2NGJlZWQ1ZTMzYTY4YjYxOTBhMDM0ODZiMWI0YjExYTNhNTkwNjg4Yzc1YTg5N2I5ZDEwZDk1In19fQ==";
        ItemStack item = ItemUtil.buildHead(base64, TextUtil.makeText(getDisplayName(), TextUtil.YELLOW).append(TextUtil.placeText()),
                List.of(TextUtil.makeText("Place and choose a workbench",TextUtil.DARK_PURPLE),
                        TextUtil.makeText("that can be used on the go", TextUtil.DARK_PURPLE)));
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        CRAFTING_KEY.setKeyOnItem(item, true);
        addUnrepairable(item);
        generateUniqueModelString(item);
        return item;
    }

    public static PortableWorkbench getInstance() {
        if (instance == null) instance = new PortableWorkbench();
        return instance;
    }

    @Override
    public boolean uponClick(PlayerInteractEvent e, PlayerData pD) {
        if (e.getClickedBlock() == null) return false;
        Player player = pD.getPlayer();
        Block block = e.getClickedBlock().getRelative(e.getBlockFace());
        if (block.getType() != Material.AIR) {
            TextUtil.errorMessage(player, "The " + getDisplayName() + " cannot be placed here!");
            return false;
        }
        new PortableWorkbenchSelectionGUI(pD, e.getItem());

        return false;
    }
}
