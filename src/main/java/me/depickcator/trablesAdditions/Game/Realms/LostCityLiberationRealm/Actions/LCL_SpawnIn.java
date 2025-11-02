package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Actions;

import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmActions;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Mobs.*;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Persistence.LocationMesh;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Random;

public class LCL_SpawnIn extends RealmActions {
    public LCL_SpawnIn(String meshName, RealmController controller) {
        super(meshName, controller);
    }

    public boolean start() {
        try {
            LocationMesh roomMesh = controller.getReader().getLocationsMesh(meshName, controller.getWorld());
            Random random = new Random();
            for (Pair<Location, Integer> spawnLoc : roomMesh.getAllLocationsWeighted()) {
                spawnMob(spawnLoc.getRight(), spawnLoc.getLeft(), random);
            }
            return true;
        } catch (IOException e) {
            TextUtil.debugText("LCL Load Spawns", e.getMessage());
            controller.stopRealm();
            return false;
        }
    }

    protected void spawnMob(int mobType, Location location, Random random) {
        location.getWorld().spawnParticle(Particle.SONIC_BOOM, location, 1, 0, 0, 0, 0);
        new BukkitRunnable() {
            @Override
            public void run() {
                getMob(mobType, location, random);
            }
        }.runTaskLater(TrablesAdditions.getInstance(), 7);

    }

    protected LivingEntity getMob(int mobType, Location location, Random random) {
        net.minecraft.world.entity.LivingEntity livingEntity = switch (mobType) {
            case 2 -> new LCL_Pillager(location, random);
            case 3 -> new LCL_ZombieVillager(location, random);
            case 4 -> new LCL_MiniGolem(location);
            case 5 -> new LCL_Witch(location);
            case 6 -> new LCL_Evoker(location);
            default -> new LCL_Vindicator(location, random); //1
        };
        return livingEntity.getBukkitLivingEntity();
    }
}
