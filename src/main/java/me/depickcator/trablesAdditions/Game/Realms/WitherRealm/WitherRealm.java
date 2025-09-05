package me.depickcator.trablesAdditions.Game.Realms.WitherRealm;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action.*;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates.*;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Sequences.StartBoss.StartBoss;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import me.depickcator.trablesAdditions.Scoreboards.WitherRealmBoard;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesMenuGUI;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;

import java.time.Instant;
import java.util.*;

public class WitherRealm extends Realm implements ScoreboardObserver {

    /*Takes Door mesh names and pairs them with the rooms that it would load*/
    private final Map<String, Set<WitherRealmActions>> roomMap;
    public static String WITHER_REALM_DUNGEON_DOOR_KEY = "wither_realm_dungeon_door";
    public static String WITHER_REALM_DUNGEON_DISCIPLE_KEY = "wither_realm_disciple";
    public static String WITHER_REALM_BLOOD_DOOR_KEY = "wither_realm_blood_door";
    private final Set<String> actionsLoaded;
    private final Map<Block, Integer> placedBlocks;
    private final Set<String> bossDisciples;
    private RealmController controller;
    private Audience audience;
    private int timeTicks;

    public WitherRealm(Location location) {
        super(location, "TestRealm", "Test Realm");
        roomMap = new HashMap<>();
        actionsLoaded = new HashSet<>();
        placedBlocks = new HashMap<>();
        bossDisciples = new HashSet<>();
        audience = Audience.audience(Bukkit.getOnlinePlayers());
    }

    @Override
    public boolean runAction(PlayerData playerData, TrablesMenuGUI trablesMenuGUI, InventoryClickEvent event) {
        WitherRealmBoard.getInstance().addObserver(this);
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
                if (actionsLoaded.add(action.getMeshName())) {
//                    actionsLoaded.add(action.getMeshName());
                    action.start();
                }
            }
        }
    }

    @Override
    public void onLoop(RealmController controller) {
        removeOldBlocks();
    }

    @Override
    public void closePortal() {
        super.closePortal();
        WitherRealmBoard.getInstance().removeObserver(this);
    }

    private void timeTick() {
        timeTicks++;
        if (timeTicks % 4 == 0) {
            getBoardMaker().updateAllViewers(this);
        }
    }

    @Override
    public void update(BoardMaker maker, Objective board, PlayerData playerData) {
        maker.editLine(board, 13, TextUtil.makeText(" Time Elapsed:"));
        maker.editLine(board, 12, TextUtil.makeText("   " + TextUtil.formatTime(timeTicks/4), TextUtil.AQUA));
    }

    public void addPlacedBlock(Block block) {
        block.setMetadata("PLACED", new FixedMetadataValue(TrablesAdditions.getInstance(), Instant.now()));
        placedBlocks.put(block, 1 + placedBlocks.getOrDefault(block, 0));
    }

    public boolean containsPlacedBlock(Block block) {
        return placedBlocks.containsKey(block);
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

    public void addDisciple(String meshName) {
        if (bossDisciples.contains(meshName)) {
            TextUtil.debugText("Wither Realm", "Already added: " + meshName + " " + bossDisciples.size());
            return;
        }
        bossDisciples.add(meshName);
        TextUtil.debugText("Wither Realm", "Newly added: " + meshName + " "  + bossDisciples.size());
    }

    public void removeDisciple(String meshName) {
        if (bossDisciples.remove(meshName)) {
            if (bossDisciples.isEmpty()) {
                audience.sendMessage(TextUtil.makeText("All disciples have been vanquished. The Blood Door can be opened", TextUtil.RED));
                audience.playSound(SoundUtil.makeSound(org.bukkit.Sound.BLOCK_BEACON_ACTIVATE, 10.0F, 1.0F), Sound.Emitter.self());
                setRealmState(new Wither_DisciplesGoneState(this));
            } else {
                audience.sendMessage(TextUtil.makeText("One of the disciples has been eliminated. " +
                        getDisciplesRemaining() + " remains", TextUtil.AQUA));
                getBoardMaker().updateAllViewers((ScoreboardObserver) getRealmState());
            }

        }
    }

    public int getDisciplesRemaining() {
        return bossDisciples.size();
    }

    @Override
    public void onStartBoss(RealmController controller) {
        new StartBoss(controller, this,
                new Wither_BossState_Phase0(this, controller)).start();
//        setRealmState(new );
    }

    @Override
    public void onBossDefeated(RealmController controller) {
        setRealmState(new Wither_RewardState(this));
        addDoor("door_boss_prize", Set.of());
        triggerDoor("door_boss_prize");
    }

    @Override
    public BoardMaker getBoardMaker() {
        return WitherRealmBoard.getInstance();
    }

    private void loadDoors() {
        addDoor("door_2", Set.of("room_2"), Set.of("chest_2"));
        addDoor("door_3m", Set.of("room_3m"), Set.of("chest_3m"), Set.of("boss_3m"));
        addDoor("door_3l", Set.of("room_3l"), Set.of("chest_3l"), Set.of("boss_3l"));
        addDoor("door_3r", Set.of("room_3r"), Set.of("chest_3r"), Set.of("boss_3r"));
        addDoor("door_3r_pillar", Set.of("room_3r_pillar"), Set.of());
        addDoor("door_3r_upper", Set.of("room_3r_upper"), Set.of());
        addDoor("door_3l_upper", Set.of("room_3l_upper"), Set.of());
        addDoor("door_3l_upper_kings", Set.of(), Set.of());
        addDoor("door_3l_chest", Set.of(), Set.of());
        addDoor("door_b2", Set.of(new WitherRealm_LoadMiniBossRoom("room_b2", controller, this)));
        addDoor("door_conn", Set.of(
                new WitherRealm_LoadSpiderRoom("room_conn", controller),
                new WitherRealm_LoadRoom("room_3l", controller),
                new WitherRealm_LoadDisciple("boss_3l", controller, this),
                new WitherRealm_FillLoot("chest_3l", controller, Material.BARREL),
                new WitherRealm_FillLoot("chest_conn", controller, Material.BARREL)));
        addDoor("door_conn_tunnel_inner", Set.of(
                new WitherRealm_LoadSpiderRoom("room_conn", controller),
                new WitherRealm_FillLoot("chest_conn", controller, Material.BARREL),
                new WitherRealm_LoadRoom("room_conn_tunnel", controller)));
        addDoor("door_conn_tunnel_outer", Set.of("room_conn_tunnel", "room_3m"), Set.of("chest_3m"), Set.of("boss_3m"));
        addDoor("door_room_grand_from_3m", Set.of("room_3m", "room_grand"), Set.of("chest_3m", "chest_grand"), Set.of("boss_3m", "boss_grand"));
        addDoor("door_grand_3r", Set.of("room_grand", "room_3r"), Set.of("chest_grand", "chest_3r"), Set.of("boss_3r", "boss_grand"));
        addDoor("door_b1", Set.of(new WitherRealm_LoadMiniBossRoom("room_b1", controller,this)));
        addDoor("door_grand_chest_door", Set.of(), Set.of());
        addDoor("door_grand_chest_lava", Set.of(), Set.of());
        addDoor("door_grand_chest_coffins", Set.of("room_grand_coffins"), Set.of());
        addDoor("door_grand_chest_graveyard", Set.of("room_grand_graveyard"), Set.of());
        addDoor("door_grand_tunnel", Set.of("room_grand_tunnel"), Set.of());
        addDoor("door_grand_tunnel_chest", Set.of(), Set.of());
        addDoor(new WitherRealm_LoadBloodDoor("door_grand_be", controller),
                parseActions(Set.of("room_grand", "room_be"), Set.of("chest_grand"), Set.of("boss_grand")));
        addDoor(new WitherRealm_LoadBloodDoor("door_be_3m", controller),
                parseActions(Set.of("room_3m", "room_be"), Set.of("chest_3m"), Set.of("boss_3m")));
        addDoor("door_be_launch", Set.of(new WitherRealm_LoadControlPanel("ui_be_launch", controller)));
    }

    private void removeOldBlocks() {
        Instant now = Instant.now();
        for (Block block : new HashSet<>(placedBlocks.keySet())) {
            if (!block.hasMetadata("PLACED")) {
                removeBlock(block);
                return;
            }
            Instant time = Instant.parse(block.getMetadata("PLACED").getFirst().asString());
            int timePassedRequirement = block.isLiquid() ? 2 : 5;
            if (now.isAfter(time.plusSeconds(timePassedRequirement))) {
                removeBlock(block);
            }
        }
    }

    private void addDoor(String doorMeshName, Set<String> roomMeshNames, Set<String> chestMeshNames) {
        addDoor(doorMeshName, roomMeshNames, chestMeshNames, Set.of());
    }

    private void addDoor(String doorMeshName, Set<String> roomMeshNames, Set<String> chestMeshNames, Set<String> discipleMeshNames) {
        addDoor(doorMeshName, parseActions(roomMeshNames, chestMeshNames, discipleMeshNames));
    }

    private Set<WitherRealmActions> parseActions(Set<String> roomMeshNames, Set<String> chestMeshNames, Set<String> discipleMeshNames) {
        if (controller == null) return null;
        Set<WitherRealmActions> actions = new HashSet<>() ;
        for (String roomMeshName : roomMeshNames) actions.add(new WitherRealm_LoadRoom(roomMeshName, controller));
        for (String chestMeshName : chestMeshNames) actions.add(new WitherRealm_FillLoot(chestMeshName, controller, Material.BARREL));
        for (String discipleMeshName : discipleMeshNames) actions.add(new WitherRealm_LoadDisciple(discipleMeshName, controller, this));
        return actions;
    }

    private void addDoor(String doorMeshName, Set<WitherRealmActions> actions) {
//        if (!new WitherRealm_LoadDoor(doorMeshName, controller).start()) return;
//        if (controller == null) return;
//        roomMap.put(doorMeshName, actions);
        addDoor(new WitherRealm_LoadDoor(doorMeshName, controller), actions);
    }

    private void addDoor(WitherRealm_LoadDoor door, Set<WitherRealmActions> actions) {
        if (!door.start()) return;
        if (controller == null) return;
        roomMap.put(door.getMeshName(), actions);
    }

    private void startAnimation(RealmController controller) {
        audience = Audience.audience(controller.getWorld().getPlayers());
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

    @Override
    public void worldRules(World world) {
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.WATER_SOURCE_CONVERSION, false);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
    }
}
