package me.depickcator.trablesAdditions.Game.Realms.WitherRealm;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmSkeleton;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmZombie;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.States.Wither_InGameState;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.States.Wither_InitialState;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Random;

public class WitherRealm extends Realm {

    public WitherRealm(Location location) {
        super(location, "TestRealm", "Test Realm");
    }

    @Override
    public boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event) {
        Player player = playerData.getPlayer();
        Location loc = player.getLocation();
        TextUtil.broadcastMessage(TextUtil.makeText(player.getName() + " has activated WitherRealm it will be placed at"
        + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ(), TextUtil.AQUA));
        if (!loc.getBlock().isLiquid()) {
            new RealmController(this).initialize();
        } else return false;
        return true;
    }

    @Override
    public void onStart(RealmController controller) {
        World world = controller.getWorld();
        try {
            LocationMesh room1Mesh = controller.getReader().getLocationsMesh("room_1", world);
            Random random = new Random();

            for (Pair<Location, Integer> spawnLoc : room1Mesh.getAllLocationsWeighted()) {
                switch (spawnLoc.getRight()) {
                    case 1 -> new WitherRealmZombie(spawnLoc.getLeft(), random);
                    case 2 -> new WitherRealmSkeleton(spawnLoc.getLeft(), random);
                }
            }

            startAnimation(controller);
            setRealmState(new Wither_InGameState(this));
        } catch (IOException e) {
            TextUtil.debugText("Wither Realm", e.getMessage());
        }
    }

    @Override
    public void onLoop(RealmController controller) {

    }

    private void startAnimation(RealmController controller) {
        Audience audience = Audience.audience(controller.getWorld().getPlayers());
        new BukkitRunnable() {
            int seconds = 5;
            @Override
            public void run() {
                if (seconds <= 0) {finish(); cancel(); return ;}
                Sound sound = Sound.sound(Registry.SOUNDS.getKey(org.bukkit.Sound.UI_BUTTON_CLICK), Sound.Source.MASTER, 10F, 2f);
                audience.playSound(sound);
                audience.sendMessage(TextUtil.makeText(getDisplayName() + " begins in " + seconds + " seconds", TextUtil.GOLD));
                seconds--;
            }
            private void finish() {
                audience.playSound(Sound.sound().type(Registry.SOUNDS.getKey(org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL)).pitch(2f)
                        .volume(10).build());
                try {
                    LocationMesh mesh = controller.getReader().getLocationsMesh("door", controller.getWorld());
                    for (Location location : mesh.getAllLocations()) {
                        Block block = location.getBlock();
                        block.setType(Material.BEDROCK);
                        location.getBlock().breakNaturally(true);
                    }
                } catch (IOException e) {
                    TextUtil.debugText("Wither Realm Error", e.getMessage());
                    controller.stopRealm();
                }
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 20);
    }

    @Override
    protected RealmStates getStartingRealmState() {
        return new Wither_InitialState(this);
    }


}
