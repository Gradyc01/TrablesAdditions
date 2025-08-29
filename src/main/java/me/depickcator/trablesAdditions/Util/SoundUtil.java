package me.depickcator.trablesAdditions.Util;

import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SoundUtil {
    public static void broadcastSound(Sound sound, float volume, double pitch) {
        broadcastSound(sound, volume, pitch, new ArrayList<Player>(Bukkit.getOnlinePlayers()));
    }

    public static void broadcastSound(Sound sound, float volume, double pitch, List<Player> playerList) {
        for (Player player : playerList) {
            player.playSound(player.getLocation(), sound, volume, (float)pitch);
        }
    }

    public static void playErrorSoundEffect(Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 0f);
    }

    public static void playHighPitchPling(Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
    }

    public static net.kyori.adventure.sound.Sound makeSound(Sound sound,  float volume, float pitch) {
         return net.kyori.adventure.sound.Sound.sound(Registry.SOUNDS.getKey(sound),
                net.kyori.adventure.sound.Sound.Source.MASTER, volume, pitch);
    }


}
