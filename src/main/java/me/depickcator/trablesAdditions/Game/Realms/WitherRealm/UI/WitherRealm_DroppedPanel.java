package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmWither;
import me.depickcator.trablesAdditions.TrablesAdditions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;

public class WitherRealm_DroppedPanel {
    private final World world;
    private final WitherRealmWither wither;

    public WitherRealm_DroppedPanel(Location location, WitherRealmWither wither) {
        this.world = location.getWorld();
        this.wither = wither;
        FallingBlock fallingBlock = (FallingBlock) world.spawnEntity(location, EntityType.FALLING_BLOCK);
        fallingBlock.setBlockData(Material.COMMAND_BLOCK.createBlockData());
        fallingBlock.setCancelDrop(true);
        checkIfOnGround(fallingBlock);
    }

    private void checkIfOnGround(FallingBlock block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!block.isValid() || block.isOnGround()) {
                    Block landedBlock = block.getLocation().getBlock();
                    landedBlock.setType(Material.COMMAND_BLOCK);
                    block.remove();
                    new WitherRealmStunControlPanel(landedBlock, wither);
                    cancel();
                }
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 2L, 2L);
    }
}
