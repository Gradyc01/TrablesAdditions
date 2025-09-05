package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmEndCrystal;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockMenuGUI;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WitherRealmLaunchControlPanelGUI extends WitherRealmControlPanelGUI {
    private final List<Location> beamPoints;
    private final Location centerPoint;
    private final List<WitherRealmEndCrystal> crystals;
    private final RealmController controller;

    public WitherRealmLaunchControlPanelGUI(Block block, RealmController controller, Location centerPoint, List<Location> beamPoints) {
        super(block, TextUtil.makeText("Initiate Launch", TextUtil.AQUA));
        this.controller = controller;
        this.beamPoints = beamPoints;
        this.crystals = new ArrayList<>();
        this.centerPoint = centerPoint;
        resetPassKey();
    }

    @Override
    protected void resetPassKey() {
        for (WitherRealmEndCrystal crystal : new ArrayList<>(crystals)) {
            crystal.ignite();
            crystals.remove(crystal);
        }
        for (Location beamPoint : beamPoints) {
            new WitherRealmEndCrystal(beamPoint, centerPoint, crystals);
        }
    }

    @Override
    protected void onClickCorrectButton(PlayerData playerData) {
        if (!crystals.isEmpty()) crystals.removeFirst().ignite();
    }

    @Override
    protected boolean hasReachSuccessfulCompletion() {
        return passKeys >= beamPoints.size();
    }

    @Override
    protected void successfullyCompleted() {
        inventory.close();
        controller.startBossFight();
        TrablesBlockGUI.removeGUI(this);
    }
}
