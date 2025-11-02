package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters;

import me.depickcator.trablesAdditions.Game.Effects.LCLTrueSight;
import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.LCLSymbol;
import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.Symbols.*;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions.*;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates.LCL_E1_End;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates.LCL_E1_Grace;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates.LCL_E1_Skirmish;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates.LCL_E1_Spawners;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Actions.TeleportToSpawns;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Entities.ItemDisplay;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class LCL_Encounter1 extends LCL_Encounter{
    private final List<String> spawnMeshNames;
    private final Set<LivingEntity> initialEntities;
    private final Set<LivingEntity> witches;
    private final List<ItemStack> symbols;
    private final List<Location> spawnerLocations;
    private final List<ItemStack> symbolPassword;
    private final List<TriggerE1Spawner> spawners;
    private final Map<Block, LCL_RegisterCleanseDoor> cleanseDoorMap;
//    private final Set<>
    private int mobSpawnTicks;
    public LCL_Encounter1(LostCityLiberationRealm realm, RealmController controller) {
        super(realm, "Encounter 1", controller);
        initialEntities = new HashSet<>();
        witches = new HashSet<>();
        symbols = initSymbols();
        this.spawners = new ArrayList<>();
        this.spawnerLocations = initSpawnerLocations();
        this.symbolPassword = generatePassword();
        this.cleanseDoorMap = new HashMap<>();
        spawnMeshNames = List.of(
                "e1_spawn_1", "e1_spawn_2", "e1_spawn_3", "e1_spawn_4", "e1_spawn_5",
                "e1_spawn_6", "e1_spawn_7", "e1_spawn_8", "e1_spawn_9", "e1_spawn_10");

        this.mobSpawnTicks = -30 * 4;
        initCleanseDoors("e1_cd_z2", "e1_cd_z1", "e1_cd_s1", "e1_cd_s2", "e1_cd_s3");
        new LCL_AddAnswerLocations("e1_answer", controller, this).start();
    }

    @Override
    protected void uponLoop() {
        spawnMobs();
    }

    public void spawnMobWave(int num, boolean witches) {
        Random r = new Random();
        TextUtil.debugText("spawnMobWave", num + " waves have been spawned including witches");
        List<String> spawns = new ArrayList<>(spawnMeshNames);
        for (int i = 0; i < Math.min(num, spawns.size()); i++) {
            int index = r.nextInt(spawns.size());
            if (witches) new LCL_RegisterWitches(spawns.get(index), controller, this.witches).start();
                else new LCL_SpawnIn(spawns.get(index), controller).start();
            spawns.remove(index);
        }
        warningSound();
    }

    private void spawnMobs() {
        if (!initialEntities.isEmpty()) return;
        if (mobSpawnTicks++ > 50 * 4 && !(realm.getRealmState() instanceof LCL_E1_Grace)) {
            mobSpawnTicks = 0;
            spawnMobWave(Math.min(4, Math.max(2, controller.getPlayingPlayers().size())), false);
        }
    }

    public void spawnPlayerIn(Player player) {
        if (!new TeleportToSpawns("portal_overworld_exit", controller, player).start()) controller.stopRealm();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 5 * 20, 0, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 0, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 5 * 20, 0, true, false));
    }

    public void spawnEvokerWave() {
        new LCL_SpawnIn("e1_spawn_1", controller).start();
        new LCL_SpawnIn("e1_spawn_2", controller).start();
        new LCL_SpawnIn("e1_spawn_3", controller).start();
        new LCL_RegisterEvoker("e1_spawn_2", controller, this).start();
    }

    public boolean attemptTriggerCleanseDoor(Block block, PlayerData playerData) {
        if (cleanseDoorMap.containsKey(block)) {
            cleanseDoorMap.get(block).start(playerData);
            return true;
        }
        return false;
    }

    @Override
    public void set() {
        super.set();
        new LCL_RegisterSpawns("e1_spawn_initial", controller, initialEntities).start();
        TextUtil.debugText("LCL Encounter 1", "Initial entities: " + initialEntities.size());
    }

    public boolean checkIfInitialEntity(LivingEntity entity) {
        if (initialEntities.contains(entity)) {
            initialEntities.remove(entity);
            if (initialEntities.isEmpty()) {
                if (controller.getPlayingPlayers().size() > 1) {
                    new LCL_SpawnIn("e1_spawn_1", controller).start();
                    new LCL_SpawnIn("e1_spawn_3", controller).start();
                }
                new LCL_SpawnIn("e1_spawn_2", controller).start();
                return true;
            }
        };
        return false;
    }

    public boolean checkIfWitches(LivingEntity entity) {
        if (witches.contains(entity)) {
            witches.remove(entity);
            return witches.isEmpty();
        };
        return false;
    }

    @Override
    public void giveTrueSight(Player player) {
        super.giveTrueSight(player);
        generateSpawners(player);
        realm.setRealmState(new LCL_E1_Spawners(realm, this));
        TextUtil.debugText(player.getName());
    }

    @Override
    public void cleanseTrueSight(Player player) {
        super.cleanseTrueSight(player);
        spawnPlayerIn(player);
        removeSpawners();
    }

    @Override
    public void removeTrueSight(Player player) {
        super.removeTrueSight(player);
        removeSpawners();
    }

    public boolean isCorrectAnswer(ItemStack itemStack) {
        if (itemStack.equals(symbolPassword.getFirst())) {
            symbolPassword.removeFirst();
            if (checkIfWin()) return true;
            realm.setRealmState(new LCL_E1_Grace(realm, this));
            return true;
        }
        return false;
    }

    private boolean checkIfWin() {
        if (symbolPassword.isEmpty()) {
//            stopLoop();
            realm.setRealmState(new LCL_E1_End(realm, this));
            return true;
        }
        return false;
    }

    private void removeSpawners() {
        clearCleanseDisplays();
        for (TriggerE1Spawner spawner : spawners) {
            spawner.remove();
        }
        Audience audience = getAudience();
        audience.sendMessage(TextUtil.makeText("The Spawners have been deactivated", TextUtil.YELLOW));
        audience.playSound(SoundUtil.makeSound(Sound.BLOCK_TRIAL_SPAWNER_CLOSE_SHUTTER, 10, 1));
        realm.setRealmState(new LCL_E1_Skirmish(realm, this));
    }

    /*Generates Spawners for every player except Player: player*/
    private void generateSpawners(Player player) {
        List<Player> players = controller.getPlayingPlayers().stream().filter(p -> !p.equals(player)).toList();
        List<ItemStack> symbols = new ArrayList<>(getSymbols());

        Random rand = new Random();
        spawners.clear();
        for (Location loc : spawnerLocations) {
            ItemStack item = symbols.get(rand.nextInt(symbols.size()));
            TriggerE1Spawner spawner = new TriggerE1Spawner(loc.getBlock(), this, item,
                    item.equals(symbolPassword.getFirst()) ? players : controller.getPlayingPlayers());
            spawners.add(spawner);
            if (item.equals(symbolPassword.getFirst())) {
                ItemDisplay display = new ItemDisplay(loc.toCenterLocation(), new ItemStack(Material.TRIAL_SPAWNER), List.of(player));
                display.setGlowingTag(true);
                display.setGlowColorOverride(0xFF5555);
                display.setCustomNameVisible(false);
                new LCL_AddCleanseLocations("e1_cleanse", controller, spawner.getEntity(), this).start();
                TextUtil.debugText("LCLE1", " Added red display at location " + TextUtil.formatLocation(loc));
            }

            symbols.remove(item);
        }
        
    }

    private void warningSound() {
        new BukkitRunnable() {
            int times = 2;
            Audience audience = Audience.audience(controller.getPlayingPlayers());
            net.kyori.adventure.sound.Sound sound = SoundUtil.makeSound(Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
            @Override
            public void run() {
                if (times <= 0) cancel();
                audience.playSound(sound);
                times--;
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 20, 8);
    }

    private void initCleanseDoors(String... meshNames) {
        for (String meshName : meshNames) {
            LCL_RegisterCleanseDoor door = new LCL_RegisterCleanseDoor(meshName, controller);
            for (Block block : door.registerCoordinates()) {
                cleanseDoorMap.put(block, door);
            }
        }
    }

    private List<ItemStack> initSymbols() {
        List<LCLSymbol> symbols = List.of(
                new LCLCore(),
                new LCLFragment(),
                new LCLHalen(),
                new LCLLunar(),
                new LCLPortal(),
                new LCLRedRing(),
                new LCLSouls(),
                new LCLStars(),
                new LCLSunset()
        );
        return symbols.stream().map(LCLSymbol::getResult).toList();
    }

    private List<Location> initSpawnerLocations() {
        try {
            LocationMesh mesh = controller.getReader().getLocationsMesh("e1_spawners", controller.getWorld());
            return mesh.getAllLocations();
        } catch (IOException e) {
            controller.stopRealm();
            return new ArrayList<>();
        }
    }

    private List<ItemStack> generatePassword() {
        List<ItemStack> items = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            items.add(symbols.get(r.nextInt(initSymbols().size())));
        }
        return items;
    }
    public Audience getAudience() {
        return Audience.audience(controller.getPlayingPlayers());
    }

    public List<Player> getPlayers() {
        return controller.getPlayingPlayers();
    }

    public List<ItemStack> getSymbols() {
        return symbols;
    }
}
