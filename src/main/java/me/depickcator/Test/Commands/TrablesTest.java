package me.depickcator.Test.Commands;

import me.depickcator.trablesAdditions.Commands.TrablesCommands;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.UI.MainMenuGUI;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
            case "start-realm" -> {
                if (sender instanceof Player player) {
                    RealmController controller = RealmController.getController(player.getWorld().getName());
                    controller.startRealm();
                }
            }
            case "end-realm" -> {
                if (sender instanceof Player player) {
                    RealmController controller = RealmController.getController(player.getWorld().getName());
                    controller.stopRealm();
                }
            }
            case "give-kit" -> {
                if (sender instanceof Player player) {
                    player.getInventory().clear();
                    String arg = args.length > 1 ?  args[1] : "0";
                    switch (arg) {
                        case "2" -> kit2(player);
                        case "3" -> kit3(player);
                        default ->  kit1(player);
                    }
                    generalKit(player);
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of("open-main-menu", "add-playerData", "remove-playerData", "start-realm", "end-realm", "give-kit");
    }

    private ItemStack enchantItem(Material material, Enchantment enchant, int level) {
        ItemStack item = new ItemStack(material);
        item.addEnchantment(enchant, level);
        return item;
    }

    private void generalKit(Player player) {
        PlayerUtil.giveItem(player,
                new ItemStack(Material.COOKED_BEEF, 64),
                new ItemStack(Material.GOLDEN_APPLE, 16),
                new ItemStack(Material.OAK_PLANKS, 64),
                new ItemStack(Material.WATER_BUCKET),
                new ItemStack(Material.WATER_BUCKET),
                new ItemStack(Material.LAVA_BUCKET),
                new ItemStack(Material.TNT, 64),
                new ItemStack(Material.LAVA_BUCKET),
                new ItemStack(Material.SHIELD),
                new ItemStack(Material.SHIELD),
                new ItemStack(Material.ARROW, 64));
    }

    private void kit1(Player player) {
        PlayerUtil.giveItem(player,
                enchantItem(Material.DIAMOND_SWORD, Enchantment.SHARPNESS, 1),
                new ItemStack(Material.COOKED_BEEF, 64),
                enchantItem(Material.BOW, Enchantment.POWER, 1),
                new ItemStack(Material.GOLDEN_APPLE, 16),
                new ItemStack(Material.OAK_PLANKS, 64),
                new ItemStack(Material.WATER_BUCKET),
                new ItemStack(Material.WATER_BUCKET),
                new ItemStack(Material.LAVA_BUCKET),
                new ItemStack(Material.TNT, 64),
                new ItemStack(Material.LAVA_BUCKET),
                new ItemStack(Material.IRON_HELMET),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.IRON_BOOTS),
                new ItemStack(Material.SHIELD),
                new ItemStack(Material.SHIELD));
    }

    private void kit2(Player player) {
        PlayerUtil.giveItem(player,
                enchantItem(Material.DIAMOND_SWORD, Enchantment.SHARPNESS, 3),
                enchantItem(Material.BOW, Enchantment.POWER, 3),
                enchantItem(Material.IRON_HELMET, Enchantment.PROTECTION, 1),
                enchantItem(Material.IRON_CHESTPLATE, Enchantment.PROTECTION, 2),
                enchantItem(Material.IRON_LEGGINGS, Enchantment.PROTECTION, 1),
                enchantItem(Material.IRON_BOOTS, Enchantment.PROTECTION, 1));
    }

    private void kit3(Player player) {
        PlayerUtil.giveItem(player,
                enchantItem(Material.DIAMOND_SWORD, Enchantment.SHARPNESS, 3),
                enchantItem(Material.BOW, Enchantment.POWER, 3),
                enchantItem(Material.DIAMOND_HELMET, Enchantment.PROTECTION, 2),
                enchantItem(Material.DIAMOND_CHESTPLATE, Enchantment.PROTECTION, 2),
                enchantItem(Material.DIAMOND_LEGGINGS, Enchantment.PROTECTION, 2),
                enchantItem(Material.DIAMOND_BOOTS, Enchantment.PROTECTION, 2));
    }
}
