package me.depickcator.trablesAdditions.Game.Effects;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Locale;

public class RevivePlayerInRealm {
    private final Player revived;
    private final Player savior;
    private final Location respawnPoint;
    private final RealmController controller;
    private final Audience audience;
    public RevivePlayerInRealm(Player revived, Player savior, RealmController controller) {
        this.revived = revived;
        this.savior = savior;
        this.controller = controller;
        this.respawnPoint = savior.getLocation().clone();
        audience = Audience.audience(controller.getPlayingPlayers());
        start();
    }

    private void start() {
        savior.playSound(SoundUtil.makeSound(Sound.ITEM_TOTEM_USE, 10f, 1f), net.kyori.adventure.sound.Sound.Emitter.self());
        audience.sendMessage(TextUtil.makeText(savior.getName() + " used a revive stone for " + revived.getName(), TextUtil.GREEN));
        revived.showTitle(TextUtil.makeTitle(TextUtil.makeText("REVIVING", TextUtil.AQUA), 0, 3, 1));
        loop();
    }

    private void loop() {
        new BukkitRunnable() {
            int seconds = 5;
            @Override
            public void run() {
                if (seconds <= 0) {stop(); cancel(); return;}
                TextUtil.sendActionBar(revived, TextUtil.makeText("Reviving in " + seconds + " seconds", TextUtil.AQUA), 20);
                revived.playSound(SoundUtil.makeSound(Sound.UI_BUTTON_CLICK, 10f, 1f), net.kyori.adventure.sound.Sound.Emitter.self());
                seconds--;
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 20);
    }

    private void stop() {
        revived.teleport(respawnPoint);
        revived.setGameMode(GameMode.SURVIVAL);
    }
}
