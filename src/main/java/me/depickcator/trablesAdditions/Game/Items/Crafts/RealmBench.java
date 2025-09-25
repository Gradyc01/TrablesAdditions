package me.depickcator.trablesAdditions.Game.Items.Crafts;

import me.depickcator.trablesAdditions.Game.Items.Crafts.PortableWorkbench.PortableWorkbenchSelectionGUI;
import me.depickcator.trablesAdditions.Game.Items.Crafts.PortableWorkbench.Workbench;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ItemClick;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.CupidEssence;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.UI.MainMenuBlockGUI;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RealmBench extends Craft {
    private static RealmBench instance;

    private RealmBench() {
        super("Realm Bench", "realm_bench");
    }

    @Override
    protected Recipe initRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, getKey()), result);
        recipe.shape("AAA", "A A", "AAA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        return recipe;
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.TRIAL_SPAWNER);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        addUnrepairable(item);
        generateUniqueModelString(item);
        return item;
    }

    @Override
    public boolean uponCrafted(CraftItemEvent e, PlayerData pD) {
        Player p = pD.getPlayer();
        if (RealmController.getController(p.getWorld().getName()) != null) return false;
        Block b = p.getTargetBlock(null, 5);
        if (e.getCurrentItem() != null) e.getCurrentItem().setAmount(0);
        clearMatrix(e.getInventory().getMatrix());
        p.closeInventory();
        b.setType(Material.FLETCHING_TABLE);
        p.playSound(SoundUtil.makeSound(Sound.BLOCK_TRIAL_SPAWNER_OPEN_SHUTTER, 1.0f, 1.0f));
        new MainMenuBlockGUI(b);
        return true;
    }

    private void clearMatrix(ItemStack[] items) {
        for (ItemStack item : items) {
            if (item == null) continue;
            item.setAmount(item.getAmount() - 1);
        }
    }

    public static RealmBench getInstance() {
        if (instance == null) instance = new RealmBench();
        return instance;
    }
}
