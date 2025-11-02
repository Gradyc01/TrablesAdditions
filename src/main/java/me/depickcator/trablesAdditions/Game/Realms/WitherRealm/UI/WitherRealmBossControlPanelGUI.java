package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Actions.BreakDoor;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates.Wither_BossState;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmEndCrystal;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

public class WitherRealmBossControlPanelGUI extends WitherRealmControlPanelGUI {
    private final WitherRealmEndCrystal crystal;
    private final String rigMeshName;
    private final RealmController controller;

    public WitherRealmBossControlPanelGUI(Block block, WitherRealmEndCrystal crystal, RealmController controller, String rigMeshName) {
        super(block, TextUtil.makeText("Disable Shield", TextUtil.AQUA));
        this.controller = controller;
        this.rigMeshName = rigMeshName;
        this.crystal = crystal;
        if (getBossState() != null) getBossState().getBossFight().addPanel(this);
    }

    @Override
    protected void resetPassKey() {
        inventory.close();
    }

    private Wither_BossState getBossState() {
        if (controller.getRealmState() instanceof Wither_BossState) {
            return (Wither_BossState) controller.getRealmState();
        } else {
            controller.stopRealm();
            TextUtil.debugText("Boss Control Panel", "Boss State is wrong");
            return null;
        }
    }

    @Override
    public boolean interactWithBlock(PlayerData playerData, Block block, PlayerInteractEvent event) {
        TextUtil.debugText("Interacting with Block", " boss state null? " + (getBossState() != null));
        TextUtil.debugText("Interacting with Block", " Can open panel " + (getBossState().canOpenPanel()));
        if (event.getAction().isRightClick() && getBossState() != null && getBossState().canOpenPanel() && inventory.getViewers().isEmpty()) {
            open(playerData.getPlayer());
        }
        return true;
    }

    @Override
    protected void onClickCorrectButton(PlayerData playerData) {
        //Do Nothing on purpose
    }

    @Override
    protected boolean hasReachSuccessfulCompletion() {
        return passKeys >= 10;
    }

    @Override
    protected void successfullyCompleted() {
        inventory.close();
        crystal.ignite();
        getBlock().setType(Material.AIR);
        new BreakDoor(rigMeshName, controller).start();
        if (getBossState() != null) getBossState().getBossFight().removePanel(this);
        TrablesBlockGUI.removeGUI(this);
    }
}
