package me.depickcator.trablesAdditions.Game.Realms;

import me.depickcator.trablesAdditions.Game.Effects.FloodBlocks;
import me.depickcator.trablesAdditions.Game.Effects.PortalFrameConverter;
import me.depickcator.trablesAdditions.Game.Effects.RealmOpeningAnimation;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import me.depickcator.trablesAdditions.Util.WorldEditUtil;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;
import java.util.UUID;

public class RealmController {
    private final Realm realm;
    private final String expendableWorldName;
    public RealmController(Realm realm) {
        this.realm = realm;
        expendableWorldName = realm.getWorldName() + "_" + UUID.randomUUID();
    }

    public void start() {
        String worldPath = realm.getWorldFilePath();
        String copiedWorldPath = "./worlds/" + realm.getWorldName();
        copyWorld(worldPath, copiedWorldPath);
        new RealmOpeningAnimation(realm.getPortalLocation(), this);
    }

    public void openPortal() {
        Location portalLocation = realm.getPortalLocation();
        World portalWorld = portalLocation.getWorld();
        portalWorld.spawnParticle(Particle.EXPLOSION, portalLocation.clone().add(0, 2,0), 200, 5, 5, 5);
        portalWorld.playSound(portalLocation, Sound.ENTITY_GENERIC_EXPLODE, 5, 1f);
        File schem = new File(realm.getPortalSchemFilePath());
        WorldEditUtil.pasteSchematic(schem, realm.getPortalLocation());
        new FloodBlocks(realm.getPortalLocation(), 1, new PortalFrameConverter(expendableWorldName)).autoFlood(new Random());
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
