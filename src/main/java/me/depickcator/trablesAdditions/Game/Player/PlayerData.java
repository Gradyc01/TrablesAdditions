package me.depickcator.trablesAdditions.Game.Player;

import com.google.gson.JsonObject;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.GrimoireBook;
import me.depickcator.trablesAdditions.Game.Player.PlayerStates.DefaultState;
import me.depickcator.trablesAdditions.Game.Player.PlayerStates.PlayerAbstractState;
import me.depickcator.trablesAdditions.Game.Player.PlayerStates.PlayerState;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.PlayerDataReader;
import me.depickcator.trablesAdditions.Persistence.PlayerDataWriter;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PlayerData {
    private final Player player;
    private final TrablesAdditions plugin;
    private final PlayerScoreboards playerScoreboards;
    private final PlayerStats playerStats;
    private final PlayerArmorEffects playerArmorEffects;
    private final PlayerInventories playerInventories;
    private PlayerState playerState;
    private PlayerState previousState;
    public PlayerData(Player player) {
        this.plugin = TrablesAdditions.getInstance();
        this.player = player;

        playerStats = new PlayerStats(this);
        playerScoreboards = new PlayerScoreboards(this);
        playerArmorEffects = new PlayerArmorEffects(this);
        playerInventories = new PlayerInventories(this);
        playerState = new DefaultState();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            TextUtil.debugText("Starting Reader...");
            CompletableFuture<JsonObject> future = new PlayerDataReader(player.getUniqueId()).read();
            future.thenAccept(this::updatePlayer).exceptionally(throwable -> {
                PlayerUtil.giveItem(player, GrimoireBook.getInstance().getResult());
                return null;
            }).whenComplete((JsonObject, throwable) -> {
                //Change Player State
            });
        });
    }

    private void updatePlayer(JsonObject jsonObject) {
        TextUtil.debugText("Updating Player...");
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            playerStats.readJson(jsonObject);
            playerInventories.readJson(jsonObject);
            playerInventories.setInventoryTo(
                                RealmController.getController(player.getWorld().getName()) != null ?
                                        PlayerInventories.REALM_INV_KEY : PlayerInventories.WORLD_INV_KEY);});
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerScoreboards getPlayerScoreboards() {
        return playerScoreboards;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public PlayerArmorEffects getPlayerArmorEffects() {
        return playerArmorEffects;
    }

    public void saveData() {
        playerInventories.saveInventory();
        TrablesAdditions.getInstance().getServer().getScheduler().runTaskAsynchronously(TrablesAdditions.getInstance(), () -> {
            new PlayerDataWriter(this).write();
        });
        playerStats.delete();
    }

    public PlayerInventories getPlayerInventories() {
        return playerInventories;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        TextUtil.debugText("Player State: " + player.getName(),"Setting Player... " + this.playerState.getStateName()
                + "  to " + playerState.getStateName());
        this.playerState.onRemove(this);
        this.previousState = this.playerState;
        this.playerState = playerState;
        this.playerState.onSet(this);
        TextUtil.debugText("Player State: " + player.getName(),"Current State: " + this.playerState.getStateName()
                + "Previous State: " + this.previousState.getStateName());
    }

    public void returnToPreviousState() {
        setPlayerState(previousState);
    }
}
