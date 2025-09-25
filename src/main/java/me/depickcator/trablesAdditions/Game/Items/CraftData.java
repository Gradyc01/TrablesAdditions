package me.depickcator.trablesAdditions.Game.Items;

import me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet.KrivonBoots;
import me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet.KrivonChestplate;
import me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet.KrivonHelmet;
import me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet.KrivonLeggings;
import me.depickcator.trablesAdditions.Game.Items.Crafts.BlackHoleContainer.BlackHoleContainer;
import me.depickcator.trablesAdditions.Game.Items.Crafts.BlackHoleContainer.BlackHoleContainerGUI;
import me.depickcator.trablesAdditions.Game.Items.Crafts.PortableWorkbench.PortableWorkbench;
import me.depickcator.trablesAdditions.Game.Items.Crafts.RealmBench;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorPiece;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.GrimoireBook;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.RepairKit;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.ReviveStone;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.XPTome;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.*;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons.*;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.Craft;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.WitherRealmEntranceKey;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.ItemComparison;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.*;

public class CraftData {
    private final Map<NamespacedKey, Craft> crafts; /*The recipeKey (recipe.getKey()) paired with the craft itself*/
    private final Map<String, ArmorPiece> armorPieces;
    private final TrablesAdditions plugin;
    public CraftData() {
        crafts = new HashMap<>();
        armorPieces = new HashMap<>();
        plugin = TrablesAdditions.getInstance();
    }

    public void registerCraft(Craft craft) {
        Recipe recipe = craft.getRecipe();
        if (recipe instanceof ShapedRecipe shaped) {
            shaped.setCategory(CraftingBookCategory.EQUIPMENT);
            crafts.put(shaped.getKey(), craft);
        }
        if (recipe instanceof ShapelessRecipe shapeless) {
            shapeless.setCategory(CraftingBookCategory.EQUIPMENT);
            crafts.put(shapeless.getKey(), craft);
        }
        plugin.getServer().addRecipe(recipe);
        TextUtil.debugText("CraftsData", "Server registered craft " + craft.getDisplayName());
    }

    public void registerArmorPiece(ArmorPiece armorPiece) {
        armorPieces.put(armorPiece.getKey(), armorPiece);
        TextUtil.debugText("CraftsData", "Server registered Armor " + armorPiece.getDisplayName());
    }

    public Craft findCraft(NamespacedKey recipeKey) {
        return crafts.get(recipeKey);
    }

    public ArmorPiece findArmorPiece(ItemStack itemStack) {
        return armorPieces.get(ItemComparison.getItemModelString(itemStack));
    }

    public void initCrafts() {
        getGlobalItems();
        getWitherRealmItems();
    }
    public List<CustomItem> getGlobalItems() {
        return List.of(PortableWorkbench.getInstance(), ZombieHeart.getInstance(), GrimoireBook.getInstance(),
                ReviveStone.getInstance(), RepairKit.getInstance(), XPTome.getInstance(), BlackHoleContainer.getInstance());
    }

    public List<CustomItem> getWitherRealmItems() {
        return List.of(MinerBlessing.getInstance(), ReaperScythe.getInstance(), Poseidon.getInstance(), IronStaff.getInstance(),
                LeapingAxe.getInstance(), AutoCrossbow.getInstance(), CupidBow.getInstance(), CompactTNT.getInstance(),
                CupidEssence.getInstance(), KrivonHandle.getInstance(), OceanCore.getInstance(), ReinforcedPlating.getInstance(),
                ShatteredQuiver.getInstance(), SpiderSilk.getInstance(), ThunderCore.getInstance(), WitherRealmEntranceKey.getInstance(),
                KrivonHelmet.getInstance(), KrivonChestplate.getInstance(), KrivonLeggings.getInstance(), KrivonBoots.getInstance(),
                WitherRealmKey.getInstance(), InfinityBoom.getInstance(), UnreleasedOrdnance.getInstance(), GrowthSprout.getInstance(),
                AdvanceGrowthCraft.getInstance());
    }

    public Collection<Craft> getAllCrafts() {
        return crafts.values();
    }
}
