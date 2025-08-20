package me.depickcator.trablesAdditions.Util;


import me.depickcator.Test.Commands.Debugger;
import me.depickcator.trablesAdditions.TrablesAdditions;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TextUtil {
    public static TextColor YELLOW = TextColor.color(255, 255, 0);
    public static TextColor GOLD = TextColor.color(0xFF, 0xAA, 0);
    public static TextColor GRAY = TextColor.color(0xAA, 0xAA, 0xAA);
    public static TextColor DARK_GRAY = TextColor.color(0x77, 0x77, 0x77);
    public static TextColor BLUE = TextColor.color(0x55, 0x55, 0xFF);
    public static TextColor AQUA = TextColor.color(0x00, 0xFF, 0xFF);
    public static TextColor RED = TextColor.color(0xFF, 0x55, 0x55);
    public static TextColor DARK_RED = TextColor.color(0xFF, 0x00, 0x00);
    public static TextColor BLACK = TextColor.color(0x22, 0x22, 0x22);
    public static TextColor DARK_GREEN = TextColor.color(0x00, 0xAA, 0x00);
    public static TextColor DARK_PURPLE = TextColor.color(0xAA, 0x00, 0xAA);
    public static TextColor PINK = TextColor.color(0xFF, 0x55, 0xFF);
    public static TextColor GREEN = TextColor.color(0x55, 0xFF, 0x55);
    public static TextColor WHITE = TextColor.color(0xFF, 0xFF, 0xFF);
    public static Component topBorder(TextColor color) {
        Component text =  Component.text("=====================================================\n").color(color);
        text = text.decoration(TextDecoration.ITALIC, false);
        return text;
    }

    public static Component bottomBorder(TextColor color) {
        Component text =  Component.text("\n=====================================================").color(color);
        text = text.decoration(TextDecoration.ITALIC, false);
        return text;
    }

    public static Title makeTitle(Component title, Component subtitle, double secFadeIn, double secStay, double secFadeOut) {
        Duration fadeIn = Duration.ofMillis((long) (secFadeIn * 1000));
        Duration stay = Duration.ofMillis((long) (secStay * 1000));
        Duration fadeOut = Duration.ofMillis((long) (secFadeOut * 1000));

        Title.Times times = Title.Times.times(fadeIn, stay, fadeOut);
        return Title.title(title, subtitle, times);
    }

    public static Title makeTitle(Component title, double secFadeIn, double secStay, double secFadeOut) {
        return makeTitle(title, TextUtil.makeText(""), secFadeIn, secStay, secFadeOut);
    }

    public static Component makeText(String text) {
        return makeText(text, TextUtil.WHITE, false, false);
    }

    public static Component makeText(String text, TextColor color) {
        return makeText(text, color, false, false);
    }

    public static Component makeText(String text, TextColor color, Boolean bold, Boolean italic) {
        Component str = Component.text(text, color);
        str = str.decoration(TextDecoration.BOLD, bold);
        str = str.decoration(TextDecoration.ITALIC, italic);
        return str;
    }

    public static void broadcastTitle(Title title) {
        broadcastTitle(title, new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    public static void broadcastTitle(Title title, List<Player> players) {
        for (Player p: players) {
            p.showTitle(title);
        }
    }


    public static void broadcastMessage(Component text) {
        Audience.audience(Bukkit.getOnlinePlayers()).sendMessage(text);
    }

    public static void broadcastMessage(Component text, List<Player> players) {
        Audience.audience(players).sendMessage(text);
    }

    public static String toRomanNumeral(int num) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] numerals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                result.append(numerals[i]);
                num -= values[i];
            }
        }

        return result.toString();
    }

    public static String toRomanNumeral(String num) {
        return toRomanNumeral(Integer.parseInt(num));
    }

    public static void sendActionBar(List<Player> players, Component message, int ticks) {
        for (Player p: players) {
            TextUtil.sendActionBar(p, message, ticks);
        }
    }

    public static void sendActionBar(Player player, Component message, int ticks) {
        // Duration is in ticks; 20 ticks = 1 second
        int interval = 20; // Send message every second to ensure it's displayed
        int repetitions = ticks / interval;

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= repetitions || !player.isOnline()) {
                    cancel(); // Stop when the duration ends or player logs out
                    return;
                }
                player.sendActionBar(message); // Send the action bar message
                count++;
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, interval); // Schedule to run every second
    }

    public static void errorMessage(Player p, String msg) {
        p.sendMessage(makeText(msg, RED));
        SoundUtil.playErrorSoundEffect(p);
    }

    public static Component rightClickText() {
        return TextUtil.makeText(" [Right Click]", TextUtil.GRAY);
    }

    public static Component applyText() {
        return TextUtil.makeText(" [Apply]", TextUtil.GRAY);
    }

    public static Component clickText() {
        return TextUtil.makeText(" [Click]", TextUtil.GRAY);
    }

    public static void debugText(String text) {
        if (Debugger.getDebuggerState()) {
            new BukkitRunnable() {
                public void run() {
                    Bukkit.getServer().broadcast(TextUtil.makeText("[Debug] " + text, TextUtil.GRAY));
                }
            }.runTask(TrablesAdditions.getInstance());
        } else {
            Logger logger = Bukkit.getLogger();
            logger.info(text);
        }
    }

    public static void debugText(String prefix, String text) {
        debugText("["+prefix+"] " + text);
    }

    public static String getItemNameString(ItemStack item) {
        return getItemNameString(item, true);
    }

    public static String getItemNameString(ItemStack item, boolean removeItemTags) {
        String displayName = getComponentString(item.displayName(), removeItemTags);
        return displayName.substring(1, displayName.length() - 1);
    }

    public static String getComponentString(Component text) {
        return getComponentString(text, false);
    }

    public static String getComponentString(Component text, boolean removeTags) {
        String displayName = PlainTextComponentSerializer.plainText().serialize(text);
        if (removeTags) {
            List<Component> textComponents = List.of(rightClickText(), applyText(), clickText());
            for (Component component : textComponents) {
                displayName = displayName.replace(getComponentString(component), "");
            }
        }
        return displayName;
    }

    public static String formatTime(int totalSeconds) {
        if (totalSeconds < 0) throw new IllegalArgumentException("Seconds cannot be negative.");

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

}
