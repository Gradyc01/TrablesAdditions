package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Sequences.StartBoss;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.GameLauncher;
import me.depickcator.trablesAdditions.Interfaces.GameSequences;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportText extends GameSequences {
    private final RealmController controller;
    public TeleportText(RealmController controller) {
        super("Teleport text");
        this.controller = controller;
    }

    @Override
    public void run(GameLauncher game) {
        Audience audience = Audience.audience(controller.getPlayingPlayers());
        new BukkitRunnable() {
            int seconds = 15;
            @Override
            public void run() {
                if (seconds <= 5 || seconds % 5 == 0) {
                    audience.sendMessage(TextUtil.makeText("Launching in " + seconds + " seconds", TextUtil.YELLOW));
                    audience.playSound(SoundUtil.makeSound(Sound.UI_BUTTON_CLICK, 10, 1), net.kyori.adventure.sound.Sound.Emitter.self());
                }
                if (--seconds <= 0) {
                    game.callback(20);
                    cancel();
                }

            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 20);
    }
}
