package me.depickcator.trablesAdditions.Game.Realms;

import me.depickcator.trablesAdditions.Game.Effects.FloodBlocks;
import me.depickcator.trablesAdditions.Game.Effects.PortalFrameConverter;
import me.depickcator.trablesAdditions.Game.Effects.RealmOpeningAnimation;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.SharedEntities.StartNPC.StartingNPC;
import me.depickcator.trablesAdditions.Persistence.RealmMeshReader;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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

    private static final Map<String, RealmController> realmControllers = new HashMap<>();
    public RealmController(Realm realm) {
        this.realm = realm;
        reader = new RealmMeshReader(realm.getMeshFilePath());
        expendableWorldName = "./worlds/" + realm.getWorldName() + "_" + UUID.randomUUID();
    }

    /*Initializes this realm and begin the opening stuff to allow players to enter soon*/
    public void initialize() {
        String worldPath = realm.getWorldFilePath();
        copyWorld(worldPath, expendableWorldName);
        new RealmOpeningAnimation(realm.getPortalLocation(), this);
    }

    /*Opens the portal*/
    public void openPortal() {
        realm.openPortal();
        new StartingNPC(this);
        new FloodBlocks(realm.getPortalLocation(), 1, new PortalFrameConverter(expendableWorldName)).autoFlood(new Random());
    }

    /*Closes the portal*/
    public void closePortal() {
        realm.closePortal();
        removeController(expendableWorldName);
    }

    /*The Realm begins */
    public void startRealm() {
        realm.onStart(this);
        loop();
        TextUtil.debugText("Realm Controller", "Started Realm: " + realm.getWorldName());
    }

    /*The Realm begins */
    public void stopRealm() {
        if (task != null) task.cancel();
        if (expendableWorld == null) {
            TextUtil.debugText("Realm Controller", "ERROR World is null when it shouldn't be: " + realm.getWorldName());
            return;
        }
        closePortal();
        expendableWorld.getPlayers().forEach(player -> {
            player.teleport(realm.getPortalLocation());
        });
        Bukkit.unloadWorld(expendableWorld, false);
        new BukkitRunnable() {
            @Override
            public void run() {
                deleteDirectory(new File(expendableWorldName));
            }
        }.runTaskLaterAsynchronously(TrablesAdditions.getInstance(), 60 * 20);
        TextUtil.debugText("Realm Controller", "Stopped Realm: " + realm.getWorldName());
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

    //    private void loop() {
//        int seconds = 30;
//        new BukkitRunnable() {
//            TextDisplay textDisplay = initTextDisplay();
//            int timePassed = 0;
//            @Override
//            public void run() {
//                textDisplay.text(
//                        TextUtil.makeText(realm.getDisplayName() + " closes in ", TextUtil.GOLD)
//                                .append(TextUtil.makeText(TextUtil.formatTime(seconds - timePassed), TextUtil.AQUA)));
//                if (timePassed++ >= seconds) {
//                    cancel();
//                    textDisplay.remove();
//                }
//            }
//        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 20);
//    }

//    private TextDisplay initTextDisplay() {
//        TextDisplay textDisplay = DisplayUtil.makeTextDisplay(
//                realm.getPortalLocation().add(2, 1, 0),
//                List.of(TextUtil.makeText("", TextUtil.GOLD)),
//                0, 0, 200);
//        textDisplay.setBillboard(Display.Billboard.CENTER);
//        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
//        return textDisplay;
//    }

    private void loop() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                realm.onLoop(RealmController.this);
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 20, 5);
    }

    public Location getSpawnLocation()  {
        try {
            return reader.getLocationsMesh("spawn", Bukkit.getWorld(expendableWorldName))
                    .getRandomLocationsFromMesh(new Random(), 1, true).getFirst();
        } catch (IOException ex) {
            closePortal();
            return null;
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
