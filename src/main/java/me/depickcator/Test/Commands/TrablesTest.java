package me.depickcator.Test.Commands;

import me.depickcator.trablesAdditions.Commands.TrablesCommands;
import me.depickcator.trablesAdditions.Game.Effects.RealmOpeningAnimation;
import me.depickcator.trablesAdditions.UI.MainMenuGUI;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrablesTest extends TrablesCommands {
    public TrablesTest() {
        super(List.of("trables-test"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            TextUtil.broadcastMessage(TextUtil.makeText("Invalid use of command", TextUtil.DARK_RED));
            return false;
        }
        String commandName = args[0];
        switch (commandName) {
            case "open-main-menu" -> {
//                new MainMenuGUI(PlayerUtil.getPlayerData((Player) sender));
                if (sender instanceof Player player) {
                    new MainMenuGUI(PlayerUtil.getPlayerData(player));
                } else if (args.length > 1) {
                    Player p = Bukkit.getPlayer(args[1]);
                    if (p != null) {
                        new MainMenuGUI(PlayerUtil.getPlayerData(p));
                    }
                }
            }
            case "add-playerData" -> {
                if (sender instanceof Player player) {
                    PlayerUtil.assignNewPlayerData(player);
                } else if (args.length > 1) {
                    Player p = Bukkit.getPlayer(args[1]);
                    if (p != null) {
                        PlayerUtil.assignNewPlayerData(p);
                    }
                }
            }
            case "remove-playerData" -> {
                if (sender instanceof Player player) {
                    PlayerUtil.removePlayerData(player);
                } else if (args.length > 1) {
                    Player p = Bukkit.getPlayer(args[1]);
                    if (p != null) {
                        PlayerUtil.removePlayerData(p);
                    }
                }
            }
            case "play-animation" -> {
                if (sender instanceof Player player) {
                    new RealmOpeningAnimation(player.getLocation());
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of("open-main-menu", "add-playerData", "remove-playerData", "play-animation");
    }
}
