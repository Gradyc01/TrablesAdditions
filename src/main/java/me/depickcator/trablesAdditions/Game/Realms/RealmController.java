package me.depickcator.trablesAdditions.Game.Realms;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

public class RealmController {
    private final Realm realm;
    public RealmController(Realm realm) {
        this.realm = realm;
    }

    public void start() {
        String worldPath = "./worlds/" + realm.getWorldName();
        String copiedWorldPath = "./expendable_worlds/" + realm.getWorldName() + "_" + UUID.randomUUID();
        copyWorld(worldPath, copiedWorldPath);
    }

    private void startRealmAnimationOpening() {

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
//            public FileVisitResult visitDirectory(Path sourceDir, BasicFileAttributes attrs) throws IOException {
//                // We don't need to handle directories here as the files are already handled
//                return FileVisitResult.CONTINUE;
//            }
                    });
                } catch (IOException e) {
                    TextUtil.debugText(e.getMessage());
                }
            }
        }.runTaskAsynchronously(TrablesAdditions.getInstance());
    }
}
