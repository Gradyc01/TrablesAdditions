package me.depickcator.trablesAdditions.Game.Realms;

import me.depickcator.trablesAdditions.Game.Effects.FloodBlocks;
import me.depickcator.trablesAdditions.Game.Effects.PortalFrameConverter;
import me.depickcator.trablesAdditions.Game.Effects.PortalFrameRemover;
import me.depickcator.trablesAdditions.Game.Effects.RealmOpeningAnimation;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Persistence.RealmMeshReader;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import me.depickcator.trablesAdditions.Util.WorldEditUtil;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class RealmController {
    private final Realm realm;
    private final String expendableWorldName;
    private final RealmMeshReader reader;
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
        new FloodBlocks(realm.getPortalLocation(), 1, new PortalFrameConverter(expendableWorldName)).autoFlood(new Random());
    }

    /*Closes the portal*/
    public void closePortal() {
        realm.closePortal();
        removeController(expendableWorldName);
    }

    /*The Realm begins */
    public void startRealm() {

    }

    public Realm getRealm() {
        return realm;
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

    public Location getSpawnLocation()  {
        try {
            return reader.getLocationsMesh("spawn", Bukkit.getWorld(expendableWorldName))
                    .getRandomItemFromList(new Random(), 1, true).getFirst();
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
        return worldCreator.createWorld();
    }
}
