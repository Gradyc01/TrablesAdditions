package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealmBossFight;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Wither_BossState_Phase2 extends Wither_BossState {
    private final Set<Material> lavaBlocks = Set.of(Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.CRACKED_STONE_BRICKS,
            Material.CHISELED_STONE_BRICKS);
    public Wither_BossState_Phase2(WitherRealm realm, RealmController controller, WitherRealmBossFight bossFight) {
        super(realm, controller, bossFight);
    }

    @Override
    public void onBlockExplode(BlockExplodeEvent event) {
        onBlockExploded(event.blockList());
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        onBlockExploded(event.blockList());
    }

    private void onBlockExploded(List<Block> blockList) {
        for (Block block : new ArrayList<>(blockList)) {
            if (!block.hasMetadata("PLACED")) {
                blockList.remove(block);
                block.removeMetadata("PLACED", TrablesAdditions.getInstance());
                if (lavaBlocks.contains(block.getType())) block.setType(Material.LAVA);
            }
        }
    }

    @Override
    public void onSet() {

    }

    @Override
    public boolean canOpenPanel() {
        TextUtil.debugText("a");
        return false;
    }

    @Override
    public void setNextBossState() {

    }

    @Override
    public String getStateName() {
        return "Boss Phase 2";
    }
}
