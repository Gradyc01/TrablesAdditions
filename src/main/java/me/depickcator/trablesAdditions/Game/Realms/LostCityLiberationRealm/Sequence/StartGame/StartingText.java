package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Sequence.StartGame;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.GameLauncher;
import me.depickcator.trablesAdditions.Interfaces.GameSequences;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class StartingText extends GameSequences {
    private final RealmController controller;
    public StartingText(RealmController controller) {
        super("StartingText");
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
                audience.playSound(SoundUtil.makeSound(org.bukkit.Sound.ENTITY_VILLAGER_AMBIENT, 10, 1), net.kyori.adventure.sound.Sound.Emitter.self());
                text.removeFirst();
                if (text.isEmpty()) {
                    game.callback(60);
                    cancel();
                }

            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 1 * 20);
    }

    private List<Component> initText() {
        return new ArrayList<>(List.of(
                TextUtil.makeText("Text1", TextUtil.DARK_GRAY),
                TextUtil.makeText("Text2", TextUtil.DARK_GRAY),
                TextUtil.makeText("Text3", TextUtil.DARK_GRAY),
                TextUtil.makeText("Text4", TextUtil.DARK_GRAY)
        ));
    }
}
