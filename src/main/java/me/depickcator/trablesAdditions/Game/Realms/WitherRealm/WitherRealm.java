package me.depickcator.trablesAdditions.Game.Realms.WitherRealm;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action.*;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.States.Wither_InGameState;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.States.Wither_InitialState;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.*;

public class WitherRealm extends Realm {

    /*Takes Door mesh names and pairs them with the rooms that it would load*/
    private final Map<String, Set<WitherRealmActions>> roomMap;
    public static String WITHER_REALM_DUNGEON_DOOR_KEY = "wither_realm_dungeon_door";
    private final Set<WitherRealmActions> roomsLoaded;
    private final Map<Block, Integer> placedBlocks;
    private RealmController controller;

    public WitherRealm(Location location) {
        super(location, "TestRealm", "Test Realm");
        roomMap = new HashMap<>();
        roomsLoaded = new HashSet<>();
        placedBlocks = new HashMap<>();
    }

    @Override
    public boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event) {
        Player player = playerData.getPlayer();
        Location loc = player.getLocation();
        TextUtil.broadcastMessage(TextUtil.makeText(player.getName() + " has activated WitherRealm it will be placed at"
        + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ(), TextUtil.AQUA));
        if (!loc.getBlock().isLiquid()) {
            controller = new RealmController(this);
            controller.initialize(); //Don't add anything past this that relies on the controller as the root may still be null

        } else return false;
        return true;
    }

    @Override
    public void onStart(RealmController controller) {
        if (!new WitherRealm_LoadRoom( "room_1", controller).start()) return;
        if (!new WitherRealm_FillLoot( "chest_1", controller, Material.BARREL).start()) return;
        startAnimation(controller);
        loadDoors();
        setRealmState(new Wither_InGameState(this));
    }

    public void triggerDoor(String doorMeshName) {
        if (roomMap.containsKey(doorMeshName) && new WitherRealm_BreakDoor(doorMeshName, controller).start()) {
            for (WitherRealmActions action : roomMap.get(doorMeshName)) {
                if (!roomsLoaded.contains(action)) {
                    roomsLoaded.add(action);
                    action.start();
                }
            }
        }
    }

    @Override
    public void onLoop(RealmController controller) {
        removeOldBlocks();
    }

    public void addPlacedBlock(Block block) {
        block.setMetadata("PLACED", new FixedMetadataValue(TrablesAdditions.getInstance(), Instant.now()));
        placedBlocks.put(block, 1 + placedBlocks.getOrDefault(block, 0));
    }

    public boolean containsPlacedBlock(Block block) {
        return placedBlocks.containsKey(block);
    }

    private void loadDoors() {
        addDoor("door_2", Set.of("room_2"), Set.of("chest_2"));
        addDoor("door_3m", Set.of("room_3m"), Set.of("chest_3m"));
        addDoor("door_3l", Set.of("room_3l"), Set.of("chest_3l"));
        addDoor("door_3r", Set.of("room_3r"), Set.of("chest_3r"));
        addDoor("door_3r_pillar", Set.of("room_3r_pillar"), Set.of());
        addDoor("door_3r_upper", Set.of("room_3r_upper"), Set.of());
        addDoor("door_3l_upper", Set.of("room_3l_upper"), Set.of());
        addDoor("door_3l_upper_kings", Set.of(), Set.of());
        addDoor("door_3l_chest", Set.of(), Set.of());
    }

    private void removeOldBlocks() {
        Instant now = Instant.now();
        for (Block block : new HashSet<>(placedBlocks.keySet())) {
            if (!block.hasMetadata("PLACED")) {
                removeBlock(block);
                return;
            }
            Instant time = Instant.parse(block.getMetadata("PLACED").getFirst().asString());
            if (now.isAfter(time.plusSeconds(5))) {
                removeBlock(block);
            }
        }
    }

    public void removeBlock(Block block) {
        if (block.getBlockData() instanceof Waterlogged waterlogged) {
            if (waterlogged.isWaterlogged()) waterlogged.setWaterlogged(false);
            else block.setType(Material.AIR);
        } else block.setType(Material.AIR);
        if (placedBlocks.containsKey(block)) {
            int amount = placedBlocks.get(block);
            if (amount > 1) placedBlocks.put(block, amount - 1);
            else placedBlocks.remove(block);
        }
        if (block.hasMetadata("PLACED")) {
            block.removeMetadata("PLACED",  TrablesAdditions.getInstance());
        }
    }

    private void addDoor(String doorMeshName, Set<String> roomMeshNames, Set<String> chestMeshNames) {
        if (controller == null) return;
        Set<WitherRealmActions> actions = new HashSet<>() ;
        if (!new WitherRealm_LoadDoor(doorMeshName, controller).start()) return;
        for (String roomMeshName : roomMeshNames) {
            actions.add(new WitherRealm_LoadRoom(roomMeshName, controller));

        }
        for (String chestMeshName : chestMeshNames) {
            actions.add(new WitherRealm_FillLoot(chestMeshName, controller, Material.BARREL));
        }
        roomMap.put(doorMeshName, actions);
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
                new WitherRealm_BreakDoor("door",controller).start();
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 20);
    }

    @Override
    protected RealmStates getStartingRealmState() {
        return new Wither_InitialState(this);
    }


}
