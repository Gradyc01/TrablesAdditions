package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Sequence.StartGame;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions.LCL_BreakDoor;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions.LCL_Detonate;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Actions.FillBlock;
import me.depickcator.trablesAdditions.Interfaces.GameLauncher;
import me.depickcator.trablesAdditions.Interfaces.GameSequences;
import org.bukkit.Material;

public class OpenGateway extends GameSequences {
    private final RealmController controller;
    public OpenGateway(RealmController controller) {
        super("Gateway Open");
        this.controller = controller;
    }

    @Override
    public void run(GameLauncher game) {
        if (!new FillBlock("portal_overworld_entry", controller, Material.END_PORTAL, controller.getWorld().getName()).start()) return;
        if (!new LCL_BreakDoor("box_well", controller).start()) return;
        if (!new LCL_Detonate("explosion_well", controller).start()) return;
        game.callback(0);
    }
}
