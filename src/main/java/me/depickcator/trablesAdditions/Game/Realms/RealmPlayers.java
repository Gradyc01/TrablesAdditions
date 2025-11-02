package me.depickcator.trablesAdditions.Game.Realms;


import me.depickcator.trablesAdditions.Game.Effects.RevivePlayerInRealm;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerInventories;
import me.depickcator.trablesAdditions.Game.Player.PlayerStates.DefaultState;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Sequences.GameOver.GameOver;
import me.depickcator.trablesAdditions.Scoreboards.DefaultBoard;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class RealmPlayers {
    private final Map<Player, Boolean> players; /*Map of the Player and true if they are alive and false if not*/
    private final List<Player> deadPlayers;
    private final Set<PlayerData> allPlayedPlayers;
    private final RealmController controller;

    public RealmPlayers(RealmController controller) {
        this.players = new HashMap<>();
        this.deadPlayers = new ArrayList<>();
        this.controller = controller;
        this.allPlayedPlayers = new HashSet<>();
    }

    /*Makes this the list of players that are playing in this world*/
    public void solidifyPlayerList(List<Player> players) {
        this.players.clear();
        players.forEach(player -> this.players.put(player, true));
    }

    public void addPlayer(PlayerData playerData) {
        this.players.put(playerData.getPlayer(), true);
        allPlayedPlayers.add(playerData);
        playerData.getPlayerInventories().setInventoryTo(PlayerInventories.REALM_INV_KEY);
    }

    public void removePlayer(PlayerData playerData) {
        if (this.players.containsKey(playerData.getPlayer())) {
            this.players.remove(playerData.getPlayer());
            this.deadPlayers.remove(playerData.getPlayer());
            playerData.getPlayerScoreboards().setBoardMaker(DefaultBoard.getInstance());
            playerData.getPlayerInventories().setInventoryTo(PlayerInventories.WORLD_INV_KEY);
        }
        if (this.players.isEmpty()) {
            controller.stopRealm();
        }
    }

    public void playerDied(Player player) {
        if (this.players.containsKey(player)) {
            PlayerUtil.getPlayerData(player).setPlayerState(new DefaultState());
            this.players.put(player, false);
            deadPlayers.add(player);
            player.setGameMode(GameMode.SPECTATOR);
            player.showTitle(TextUtil.makeTitle(TextUtil.makeText("YOU DIED", TextUtil.RED), 0, 3, 1));
            TextUtil.debugText("Realm Players", "Player " + player.getName() + " died");
        }
        checkEndGame();
    }

    public boolean attemptToRevive(Player savior) {
        if (this.deadPlayers.isEmpty()) return false;
        for (Player player : new ArrayList<>(this.deadPlayers)) {
            deadPlayers.remove(player);
            if (player.getWorld().equals(this.controller.getWorld())) {
                this.players.put(player, true);
                new RevivePlayerInRealm(player, savior, controller);
                return true;
            }
        }
        return false;
    }

    private void checkEndGame() {
        for (Map.Entry<Player, Boolean> entry : this.players.entrySet()) {
            Player player = entry.getKey();
            boolean alive = entry.getValue();
            if (alive && player.getWorld() == controller.getWorld()) {
                return;
            }
        }
        new GameOver(controller).start();
    }

    public void gameEnded() {
        deadPlayers.forEach(player -> player.setGameMode(GameMode.SURVIVAL));
        TrablesAdditions.getInstance().getServer().getScheduler().runTaskAsynchronously(TrablesAdditions.getInstance(), this::splitInventories);
    }

    private void splitInventories() {
        for (PlayerData playerData : this.allPlayedPlayers) {
            PlayerInventories pI = playerData.getPlayerInventories();
            ItemStack[] realm = pI.getCorrespondingInventoryContents(PlayerInventories.REALM_INV_KEY);
            ItemStack[] content = pI.getCorrespondingInventoryContents(PlayerInventories.REWARDS);
            int contentIndex = 0;
            for (int i = 9; i <= 35; i++) {
                ItemStack item = realm[i];
                if (item == null || item.getType() == Material.AIR) continue;
                for (int j = contentIndex; j < content.length; j++) {
                    if (content[j] == null || content[j].getType() == Material.AIR) {
                        realm[i] = new ItemStack(Material.AIR);
                        content[j] = item;
                        break;
                    }
                    contentIndex++;
                }
            }
            pI.setContent(PlayerInventories.REALM_INV_KEY, realm);
            pI.setContent(PlayerInventories.REWARDS, content);
        }
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players.keySet());
    }
}
