package me.depickcator.trablesAdditions.Game.Realms;


import me.depickcator.trablesAdditions.Game.Effects.RevivePlayerInRealm;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Sequences.GameOver.GameOver;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.*;
import java.util.stream.Collectors;

public class RealmPlayers {
    private final Map<Player, Boolean> players; /*Map of the Player and true if they are alive and false if not*/
    private final List<Player> deadPlayers;
    private final RealmController controller;

    public RealmPlayers(RealmController controller) {
        this.players = new HashMap<>();
        this.deadPlayers = new ArrayList<>();
        this.controller = controller;
    }

    /*Makes this the list of players that are playing in this world*/
    public void solidifyPlayerList(List<Player> players) {
        this.players.clear();
        players.forEach(player -> this.players.put(player, true));
    }

    public void playerDied(Player player) {
        if (this.players.containsKey(player)) {
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
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players.keySet());
    }
}
