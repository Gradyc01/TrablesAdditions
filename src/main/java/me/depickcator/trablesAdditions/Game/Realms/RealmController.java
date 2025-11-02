package me.depickcator.trablesAdditions.Game.Realms;

import me.depickcator.trablesAdditions.Game.Effects.FloodBlocks;
import me.depickcator.trablesAdditions.Game.Effects.PortalFrameConverter;
import me.depickcator.trablesAdditions.Game.Effects.RealmOpeningAnimation;
import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerStates.DefaultState;
import me.depickcator.trablesAdditions.Game.Player.PlayerStats;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Entities.StartingNPC;
import me.depickcator.trablesAdditions.Listeners.DimensionalTravel;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.Persistence.RealmMeshReader;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.DisplayUtil;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class RealmController {
    private final Realm realm;
    private final String expendableWorldName;
    private final RealmMeshReader reader;
    private World expendableWorld;
    private BukkitTask task;
    private BukkitTask portalLoop;
    private List<TextDisplay> portalTimeDisplay;
    private boolean isPortalOpen;
    private boolean realmStopped;
    private final RealmPlayers realmPlayers;

    private static final Map<String, RealmController> realmControllers = new HashMap<>();
    public RealmController(Realm realm) {
        this.realm = realm;
        this.isPortalOpen = false;
        this.realmStopped = false;
        reader = new RealmMeshReader(realm.getMeshFilePath());
        expendableWorldName = "./worlds/" + realm.getWorldName() + "_" + realm.getUUID();
        realmPlayers = new RealmPlayers(this);
    }

    /*Initializes this realm and begin the opening stuff to allow players to enter soon*/
    public void initialize() {
        String worldPath = realm.getWorldFilePath();
        copyWorld(worldPath, expendableWorldName);
        new RealmOpeningAnimation(realm.getPortalLocation(), this, realm);
    }

    /*Opens the portal*/
    public void openPortal() {
        realm.openPortal();
        isPortalOpen = true;
        portalLoop();
        new StartingNPC(this);
        new FloodBlocks(realm.getPortalLocation(), 1, new PortalFrameConverter(expendableWorldName, realm.getPortalLocation())).autoFlood(new Random());
    }

    /*Closes the portal*/
    private void closePortal() {
        realm.closePortal();
        isPortalOpen = false;
        if (portalTimeDisplay != null) portalTimeDisplay.forEach(TextDisplay::remove);
        if (portalLoop != null) portalLoop.cancel();
    }

    /*The Realm begins (Aka. when the players are about to start the game) */
    public void startRealm() {
        closePortal();
        realm.onStart(this);
        gameLoop();
        TextUtil.debugText("Realm Controller", "Started Realm: " + realm.getWorldName());
    }

    /*The Realm stops */
    public void stopRealm() {
        if (realmStopped) return;
        realmStopped = true;
        if (task != null) task.cancel();
        if (expendableWorld == null) {
            TextUtil.debugText("Realm Controller", "ERROR World is null when it shouldn't be: " + realm.getWorldName());
            return;
        }
        closePortal();
        realmPlayers.gameEnded();
        expendableWorld.getPlayers().forEach(player -> {
            leaveWorld(PlayerUtil.getPlayerData(player), true);
        });
        realm.onEnd(this);
        Bukkit.unloadWorld(expendableWorld, false);
        new BukkitRunnable() {
            @Override
            public void run() {
                deleteDirectory(new File(expendableWorldName));
            }
        }.runTaskLaterAsynchronously(TrablesAdditions.getInstance(), 60 * 20);
        TextUtil.debugText("Realm Controller", "Stopped Realm: " + realm.getWorldName());
        removeController(expendableWorldName);
    }

    public void startBossFight() {
        realm.onStartBoss(this);
    }

    public void bossDefeated() {
        realmPlayers.getPlayers().forEach(p -> {
            PlayerUtil.getPlayerData(p).getPlayerStats().addNumberStat(PlayerStats.STAT_REALMS_CONQUERED, 1);});
        realm.onBossDefeated(this);
    }

    public void createExitPortal() {
        try {
            LocationMesh mesh = getReader().getLocationsMesh("door_exit", getWorld());
            for (Pair<Location, Integer> pair : mesh.getAllLocationsWeighted()) {
                Block block = pair.getLeft().getBlock();
                Orientable data = (Orientable) Material.NETHER_PORTAL.createBlockData();
                data.setAxis(pair.getRight() % 2 == 0 ? Axis.Z : Axis.X);
                block.setBlockData(data, false);
                block.setMetadata(DimensionalTravel.DIMENSIONAL_TRAVEL_KEY,
                        new FixedMetadataValue(TrablesAdditions.getInstance(), expendableWorldName));
            }
        } catch (IOException e) {
            TextUtil.debugText("Exit Portal Creation", e.getMessage());
            stopRealm();
        }
    }

    public RealmStates getRealmState() {
        return realm.getRealmState();
    }

    public World getWorld() {
        return expendableWorld;
    }

    public RealmMeshReader getReader() {
        return reader;
    }

    public RealmPlayers getRealmPlayers() {
        return realmPlayers;
    }

    public List<Player> getPlayingPlayers() {
        return realmPlayers.getPlayers();
    }

    private void portalLoop() {
        int seconds = 120;

        portalTimeDisplay = initTextDisplay();
        portalLoop = new BukkitRunnable() {
//            TextDisplay textDisplay = initTextDisplay();
            int timePassed = 0;
            @Override
            public void run() {
                for (TextDisplay textDisplay : portalTimeDisplay) {
                    textDisplay.text(
                            TextUtil.makeText(realm.getDisplayName() + " closes in ", TextUtil.GOLD)
                                    .append(TextUtil.makeText(TextUtil.formatTime(seconds - timePassed), TextUtil.AQUA)));
                }
                checkPortal();
                if (timePassed++ >= seconds) {
                    cancel();
                    for (TextDisplay textDisplay : portalTimeDisplay) {
                        textDisplay.remove();
                    }
                    stopRealm();
                }
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 20);
    }

    private List<TextDisplay> initTextDisplay() {
        Location loc = realm.getPortalLocation();
        Vector v = loc.getDirection();
        int x = Math.abs(v.getX()) < Math.abs(v.getZ()) ? 0 : 2;
        int z = Math.abs(v.getX()) < Math.abs(v.getZ()) ? 2 : 0;
        List<TextDisplay> textDisplays = new ArrayList<>();
        for  (int i = -1; i <= 1; i+=2) {
            TextDisplay textDisplay = DisplayUtil.makeTextDisplay(
                    loc.clone().add(x * i, 1, z * i),
                    List.of(TextUtil.makeText("", TextUtil.GOLD)),
                    0, 0, 200);
            textDisplay.setBillboard(Display.Billboard.CENTER);
            textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
            textDisplays.add(textDisplay);
        }
        return textDisplays;
    }

    private void gameLoop() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                realm.onLoop(RealmController.this);
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 20, 5);
    }

    public void joinWorld(PlayerData playerData) {
        try {
            Player player = playerData.getPlayer();
            Pair<Location, Integer> pair = reader.getLocationsMesh("spawn", Bukkit.getWorld(expendableWorldName))
                    .getRandomLocationsWeightedFromMesh(new Random(), 1, true).getFirst();
            Location loc = pair.getLeft().clone();
            player.teleport(loc);
            TextUtil.debugText(pair.getRight() + "");
            player.setRotation(90 * pair.getRight(), 0);
            playerData.getPlayerScoreboards().setBoardMaker(realm.getBoardMaker());
            realmPlayers.addPlayer(playerData);
        } catch (IOException ex) {
            closePortal();
        }
    }

    public void leaveWorld(PlayerData playerData, boolean teleportOut) {
        Player player = playerData.getPlayer();
        playerData.setPlayerState(new DefaultState());
        if (teleportOut) player.teleport(realm.getPortalLocation());
        realmPlayers.removePlayer(playerData);
    }

    public void leaveWorld(PlayerData playerData) {
        leaveWorld(playerData, false);
    }

    private void checkPortal() {
        if (realm.getPortalLocation().getBlock().getType() != Material.NETHER_PORTAL) {
            stopRealm();
        }
    }

    private void copyWorld(String sourcePath, String targetPath) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Path sourceDir = Paths.get(sourcePath);
                    Path targetDir = Paths.get(targetPath);
                    if (!Files.exists(targetDir)) Files.createDirectories(targetDir);

                    Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes attrs) throws IOException {
                            Path targetFile = targetDir.resolve(sourceDir.relativize(sourceFile));
                            Files.createDirectories(targetFile.getParent());
                            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            loadWorld(targetPath);
                        }
                    }.runTask(TrablesAdditions.getInstance());
                } catch (IOException e) {
                    TextUtil.debugText(e.getMessage());
                }
            }
        }.runTaskAsynchronously(TrablesAdditions.getInstance());
    }

    private World loadWorld(String path) {
        WorldCreator worldCreator = new WorldCreator(path);
        addController(expendableWorldName, this);
        expendableWorld = worldCreator.createWorld();
        realm.worldRules(expendableWorld);
        return expendableWorld;
    }

    private boolean deleteDirectory(File directory) {
//        File directory = new File(expendableWorldName);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    TextUtil.debugText("Deleting file: " + file.getName());
                    if (file.isDirectory()) {
                        deleteDirectory(file);  // Recursively delete subdirectories
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return directory.delete();  // Finally delete the directory itself
    }

    public boolean isRealmStopped() {
        return realmStopped;
    }

    public static RealmController addController(String worldName, RealmController realmController) {
        return realmControllers.put(worldName, realmController);
    }

    public static RealmController removeController(String worldName) {
        return realmControllers.remove(worldName);
    }

    public static RealmController getController(String worldName) {
        return realmControllers.get(worldName);
    }
}
