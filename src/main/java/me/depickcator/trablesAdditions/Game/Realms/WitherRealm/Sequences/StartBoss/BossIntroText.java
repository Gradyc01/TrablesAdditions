package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Sequences.StartBoss;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.GameLauncher;
import me.depickcator.trablesAdditions.Interfaces.GameSequences;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BossIntroText extends GameSequences {
    private final RealmController controller;
    public BossIntroText(RealmController controller) {
        super("Boss intro text");
        this.controller = controller;
    }

    @Override
    public void run(GameLauncher game) {
        Audience audience = Audience.audience(controller.getPlayingPlayers());
        new BukkitRunnable() {
            List<Component> text = initText();
            @Override
            public void run() {
                audience.sendMessage(text.getFirst());
                audience.playSound(SoundUtil.makeSound(Sound.UI_BUTTON_CLICK, 10, 1), net.kyori.adventure.sound.Sound.Emitter.self());
                text.removeFirst();
                if (text.isEmpty()) {
                    game.callback(60);
                    cancel();
                }

            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 10 * 20);
    }

    private List<Component> initText() {
        return new ArrayList<>(List.of(
                TextUtil.makeText("text1", TextUtil.DARK_GRAY),
                TextUtil.makeText("text2", TextUtil.DARK_GRAY),
                TextUtil.makeText("text3", TextUtil.DARK_GRAY),
                TextUtil.makeText("text4", TextUtil.DARK_GRAY)
        ));
    }
}
