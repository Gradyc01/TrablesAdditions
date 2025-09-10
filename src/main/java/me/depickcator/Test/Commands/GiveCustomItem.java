package me.depickcator.Test.Commands;

import me.depickcator.trablesAdditions.Commands.TrablesCommands;
import me.depickcator.trablesAdditions.Game.Items.Interfaces.CustomItem;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.RepairKit;
import me.depickcator.trablesAdditions.Game.Items.Uncraftable.ReviveStone;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Materials.*;
import me.depickcator.trablesAdditions.Game.Items.WitherRealm.Weapons.IronStaff;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class GiveCustomItem extends TrablesCommands {
    private final TrablesAdditions plugin;
    public GiveCustomItem() {
        super(List.of("giveCustomItem"));
        plugin = TrablesAdditions.getInstance();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player p = ((Player) commandSender).getPlayer();
        if (p == null || strings.length == 0 || strings.length > 3) return false;

        Collection<CustomItem> allItems = getAllCustomItems();
        if (strings[0].equals("help")) {
            Component text = TextUtil.makeText("  All Item strings: ", TextUtil.GRAY);
            for (CustomItem i : allItems) {
                text = text.append(TextUtil.makeText("   [" + i.getKey() + "]", TextUtil.GRAY));
            }
            p.sendMessage(text);
            return true;
        }

        List<Player> players = new ArrayList<>();

        String name = strings[1];
        if (strings[0].equals("@a")) {
            players.addAll(Bukkit.getOnlinePlayers());
        } else if (strings[0].equals("@s")) {
            players.add(p);
        } else {
            if (Bukkit.getPlayer(strings[0]) != null) players.add(Bukkit.getPlayer(strings[0]));
        }

        for (CustomItem customItem : allItems) {
            if (customItem.getKey().equals(name)) {
                ItemStack item = customItem.getResult();
                int count = strings.length == 2 ? 1 : Integer.parseInt(strings[2]);
//                if (customItem instanceof Summonable summonable) {
//                    summonable.summonEffect(p);
//                    summonable.initEntity(p);
//                    return true;
//                }
                for (int i = 0; i < count; i++) {
                    for (Player player : players) {
                        PlayerUtil.giveItem(player, item);
                    }
                }
                p.sendMessage(TextUtil.makeText("Item Found!", TextUtil.DARK_GREEN));
                return true;
            }
        }
        TextUtil.errorMessage(p, "Unable to find unlock");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> arr = new ArrayList<>();
        if (strings.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                arr.add(p.getName());
            }
        } else if (strings.length == 2) {
            for (CustomItem i : getAllCustomItems()) {
//                for (Craft c : craft) {
//                    arr.add(c.getKey());
//                }
                arr.add(i.getKey());
            }
        }

        return arr;
    }

    private Collection<CustomItem> getAllCustomItems() {
        Collection<CustomItem> allItems = new ArrayList<>();
        allItems.addAll(plugin.getCraftData().getAllCrafts());
        allItems.addAll(List.of(ReviveStone.getInstance(), RepairKit.getInstance()));
        allItems.addAll(Set.of(CompactTNT.getInstance(), CupidEssence.getInstance(),
                KrivonHandle.getInstance(), OceanCore.getInstance(),
                ReinforcedPlating.getInstance(), ShatteredQuiver.getInstance(),
                SpiderSilk.getInstance(), ThunderCore.getInstance(), ZombieHeart.getInstance(),
                IronStaff.getInstance()));
        return allItems;
    }
}
