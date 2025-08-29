package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Sequences.GameOver;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.GameLauncher;
import me.depickcator.trablesAdditions.Interfaces.GameSequences;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.title.Title;

public class EndGameText extends GameSequences {
    private final RealmController controller;
    public EndGameText(RealmController controller) {
        super("End Game Text");
        this.controller = controller;
    }

    @Override
    public void run(GameLauncher game) {
        Audience audience = Audience.audience(controller.getPlayingPlayers());
        Title title = TextUtil.makeTitle(TextUtil.makeText("GAME OVER", TextUtil.RED, true, false),
                0, 10, 1);
        audience.showTitle(title);
        game.callback(10 * 20);
    }
}
