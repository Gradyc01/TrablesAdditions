package me.depickcator.trablesAdditions.Game.Items.Interfaces;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.UseCooldown;
import me.depickcator.trablesAdditions.TrablesAdditions;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomItem {
    private final String DISPLAY_NAME;
    private final String KEY;
    private ItemStack result;
    protected final TrablesAdditions plugin;
    public CustomItem(String displayName, String key) {
        this.DISPLAY_NAME = displayName;
        this.KEY = key;
        plugin = TrablesAdditions.getInstance();
        result = initResult();
    }

//    public CustomItem(String displayName, String key, boolean dontGenerateResult) {
//        this.DISPLAY_NAME = displayName;
//        this.KEY = key;
//    }

    /*Initialize the Custom Item Result and Returns the ItemStack*/
    protected abstract ItemStack initResult();

    protected void addCooldownGroup(ItemStack item, float seconds) {
        item.setData(DataComponentTypes.USE_COOLDOWN,
                UseCooldown.useCooldown(seconds)
                        .cooldownGroup(Key.key(TrablesAdditions.getInstance().getName().toLowerCase()+ ":" + getKey())));
    }

    protected void addCooldownGroup(ItemStack item) {
        addCooldownGroup(item, 0.01f);
    }

    /*Generates a unique model number for ItemStack item*/
    protected void generateUniqueModelString(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        CustomModelDataComponent component = meta.getCustomModelDataComponent();
        component.setStrings(new ArrayList<>(List.of(KEY)));
        meta.setCustomModelDataComponent(component);
        item.setItemMeta(meta);
    }

    /*Sets a model number for ItemStack item*/
    protected void setModelString(ItemStack item, String modelString) {
        ItemMeta meta = item.getItemMeta();
        CustomModelDataComponent component = meta.getCustomModelDataComponent();
        component.setStrings(new ArrayList<>(List.of(KEY)));
        meta.setCustomModelDataComponent(component);
        item.setItemMeta(meta);
    }

    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    public String getKey() {
        return KEY;
    }

    public ItemStack getResult() {
        return result.clone();
    }

    public ItemStack getResult(int amount) {
        ItemStack item = result.clone();
        item.setAmount(amount);
        return item;
    }
}
