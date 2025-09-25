package me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.*;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.CompactTNT;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.GrowthSprout;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ShatteredQuiver;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.ZombieHeart;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.ItemUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AdvancedGrowth extends GrowthSprout {
    private static AdvancedGrowth instance;
    private AdvancedGrowth() {
        super("Soul Growth", "advance_growth");
    }

    @Override
    protected ItemStack initResult() {
        ItemStack item = new ItemStack(Material.PALE_OAK_SAPLING);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.makeText(getDisplayName(), TextUtil.DARK_GREEN).append(TextUtil.applyText()));
        meta.lore(List.of(
                TextUtil.makeText("A new wave of green has flourished", TextUtil.DARK_PURPLE),
                TextUtil.makeText("into the realm and you've imbued ", TextUtil.DARK_PURPLE),
                TextUtil.makeText("it with the ", TextUtil.DARK_PURPLE)
                        .append(TextUtil.makeText("souls of the dead", TextUtil.WHITE, true, false))));
        meta.setMaxStackSize(1);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        generateUniqueModelString(item);
        return item;
    }

    public static AdvancedGrowth getInstance() {
        if (instance == null) {
            instance = new AdvancedGrowth();
        }
        return instance;
    }

    @Override
    protected AttributeModifier createModifier(ItemStack item) {
        NamespacedKey key = new NamespacedKey(TrablesAdditions.getInstance(), getKey() + ItemComparison.itemParser(item));
        return new AttributeModifier(key, 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ARMOR);
    }

    @Override
    public String getReforgeName() {
        return "SOUL GROWTH";
    }
}
