package me.depickcator.trablesAdditions.Game.Player;

import com.google.gson.JsonObject;
import com.mojang.authlib.yggdrasil.TextureUrlChecker;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.PlayerReadable;
import me.depickcator.trablesAdditions.Persistence.PlayerWritable;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Base64;

public class PlayerInventories implements PlayerWritable, PlayerReadable {
    private final PlayerInventory inventory;
    private ItemStack[] worldInventoryContent;
    private ItemStack[] realmInventoryContent;
    private ItemStack[] rewardContent;
    public final static String WORLD_INV_KEY = "world";
    public final static String REALM_INV_KEY = "realm";
    public final static String REWARDS = "rewards";
    public final static int REWARDS_SIZE = 200;

    private String currentInventory;
    private final Player player;
    public PlayerInventories(PlayerData playerData) {
        this.player = playerData.getPlayer();
        this.inventory = player.getInventory();
        worldInventoryContent = player.getInventory().getContents();
        realmInventoryContent = new ItemStack[player.getInventory().getContents().length];
        rewardContent = new ItemStack[REWARDS_SIZE];
        currentInventory = WORLD_INV_KEY;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty(WORLD_INV_KEY, encodeInventory(worldInventoryContent));
        json.addProperty(REALM_INV_KEY, encodeInventory(realmInventoryContent));
        json.addProperty(REWARDS, encodeInventory(rewardContent));
        return json;
    }

    @Override
    public void readJson(JsonObject jsonObject) {
        if (jsonObject.has("inventories")) {
            JsonObject object = jsonObject.getAsJsonObject("inventories");
            worldInventoryContent = decodeInventory(object.get(WORLD_INV_KEY).getAsString());
            realmInventoryContent = decodeInventory(object.get(REALM_INV_KEY).getAsString());
            rewardContent = decodeInventory(object.get(REWARDS).getAsString());
        }
    }

    public void saveInventoryTo(String inventoryName) {
        try {
            saveInventory(getCorrespondingInventoryContents(inventoryName));
        } catch (IllegalArgumentException ex) {
            TextUtil.debugText("Player Inventory " + player.getName(),"ERROR: " + ex.getMessage());
        }
    }

    public void saveInventory() {
        saveInventoryTo(currentInventory);
    }

    public void setInventoryTo(String inventoryName) {
        if (!inventoryName.equals(currentInventory)) saveInventoryTo(currentInventory);
        try {
            copyInventory(getCorrespondingInventoryContents(inventoryName));
        } catch (IllegalArgumentException ex) {
            TextUtil.debugText("Player Inventory " + player.getName(),"ERROR: " + ex.getMessage());
        }
        setCurrentInventory(inventoryName);
    }

    public void setContent(String inventoryName, ItemStack[] content) {
//        ItemStack[] arr = getCorrespondingInventoryContents(inventoryName);
        setContent(getCorrespondingInventoryContents(inventoryName), content);
    }

    private void setContent(ItemStack[] arr, ItemStack[] content) {
        if (content.length != arr.length) {
            throw new IllegalArgumentException("The corresponding arr and content arr need to be of same length");
        } else {
            System.arraycopy(content, 0, arr, 0, content.length);
        }
    }

    public ItemStack[] getCorrespondingInventoryContents(String inventoryName) {
        return switch (inventoryName) {
            case WORLD_INV_KEY -> worldInventoryContent;
            case REALM_INV_KEY -> realmInventoryContent;
            case REWARDS -> rewardContent;
            default -> throw new IllegalArgumentException("Invalid inventory name");
        };
    }

    public ItemStack getCorrespondingInventoryContent(String inventoryName, int index) {
        return getCorrespondingInventoryContents(inventoryName)[index];
    }

    private void setCurrentInventory(String currentInventory) {
        TextUtil.debugText("Player Inventory" + player.getName(), "Set current inventory to: " + currentInventory);
        this.currentInventory = currentInventory;
    }



    private void copyInventory(ItemStack[] inventory) {
        TextUtil.debugText("Player Inventory " + player.getName(), "Copied inventory");
        ItemStack[] invContents = this.inventory.getContents();
        for (int i = 0; i < Integer.min(invContents.length, inventory.length); i++) {
            invContents[i] = inventory[i];
        }
        this.inventory.setContents(invContents);
    }

    private void saveInventory(ItemStack[] contents) {
        ItemStack[] invContents = inventory.getContents();
        for (int i = 0; i < Integer.max(contents.length, invContents.length); i++) {
            ItemStack newItem = i < invContents.length ? invContents[i] : null;
            if (i < contents.length) {
                contents[i] = newItem;
            } else {
                TextUtil.debugText("Player Inventories ", "ERROR: Contents isn't long enough dingus");
            }
        }
    }

    private ItemStack[] decodeInventory(String base64) {
        byte[] data = Base64.getDecoder().decode(base64);
        return ItemStack.deserializeItemsFromBytes(data);
    }

    private String encodeInventory(ItemStack[] inventory) {
        return Base64.getEncoder().encodeToString(ItemStack.serializeItemsAsBytes(inventory));
    }




}
