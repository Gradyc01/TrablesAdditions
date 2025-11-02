package me.depickcator.trablesAdditions.Game.Realms.Interfaces;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerStats;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.WitherRealm;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractRealmStates implements RealmStates, ScoreboardObserver {
    private final Set<CreatureSpawnEvent.SpawnReason> reasons = Set.of(
            CreatureSpawnEvent.SpawnReason.BUILD_WITHER, CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM,
            CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN);
    private final Realm realm;
    private final String REALM_KILLS_KEY;
    private final String REALM_DEATHS_KEY;
    public static final String PLACED_BLOCK_TAG = "PLACED_BLOCK";
    public AbstractRealmStates(Realm realm) {
        this.realm = realm;
        REALM_KILLS_KEY = realm.getWorldName() + realm.getUUID() + "_kills";
        REALM_DEATHS_KEY = realm.getWorldName() + realm.getUUID() + "_deaths";
    }

    @Override
    public void onPlayerBucket(PlayerBucketEvent event) {
        ItemStack bucketResult = event.getItemStack();
        Block targetBlock = event.getBlock();
        if (bucketResult != null && bucketResult.getType().equals(Material.BUCKET)) {
            if (event.getBucket().equals(Material.WATER_BUCKET)) targetBlock = treatWaterLogging(event);
            if (targetBlock != null) {
                realm.addPlacedBlock(targetBlock);
            }
        } else {
            if (realm.containsPlacedBlock(targetBlock)) {
                realm.removeBlock(targetBlock);
            }
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event, RealmController controller) {
        event.setCancelled(true);
        controller.getRealmPlayers().playerDied(event.getPlayer());
        if (event.deathMessage() !=null) TextUtil.broadcastMessage(event.deathMessage().color(TextUtil.DARK_RED));
        PlayerData pD = PlayerUtil.getPlayerData(event.getPlayer());
        pD.getPlayerStats().addNumberStat(REALM_DEATHS_KEY, 1);
        pD.getPlayerScoreboards().updateBoard(this);
    }

    @Override
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (reasons.contains(event.getSpawnReason()) || event.getEntityType().getKey().getKey().toLowerCase().contains("boat")) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event, RealmController controller) {
        controller.joinWorld(PlayerUtil.getPlayerData(event.getPlayer()));
        new PlayerDeathEvent(event.getPlayer(), DamageSource.builder(DamageType.OUT_OF_WORLD).build(),
                List.of(), 0, 0, TextUtil.makeText(""), false).callEvent();
    }

    @Override
    public void onPlayerLeave(PlayerQuitEvent event, RealmController controller) {
        controller.leaveWorld(PlayerUtil.getPlayerData(event.getPlayer()));
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!block.hasMetadata(PLACED_BLOCK_TAG)) {
            event.setCancelled(true);
        }
        block.removeMetadata(PLACED_BLOCK_TAG, TrablesAdditions.getInstance());
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType().equals(Material.TNT)) {
            Block placedBlock = event.getBlockPlaced();
            placedBlock.setType(Material.AIR);
            TNTPrimed tnt = (TNTPrimed) placedBlock.getWorld().spawnEntity(placedBlock.getLocation().clone().add(0.5, 0 , 0.5), EntityType.TNT);
            tnt.setSource(event.getPlayer());
            return;
        }
        realm.addPlacedBlock(block);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {
//        if (event.getEntity().hasMetadata(WitherRealm.WITHER_REALM_DUNGEON_DISCIPLE_KEY)) {
//            String name = event.getEntity().getMetadata(WitherRealm.WITHER_REALM_DUNGEON_DISCIPLE_KEY).getFirst().asString();
//            realm.removeDisciple(name);
//        }
        if (event.getDamageSource().getCausingEntity() instanceof Player player) {
            PlayerData pD = PlayerUtil.getPlayerData(player);
            pD.getPlayerStats().addNumberStat(REALM_KILLS_KEY, 1);
            pD.getPlayerScoreboards().updateBoard(this);
        }
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        onBlockExploded(event.blockList());
    }

    @Override
    public void onBlockExplode(BlockExplodeEvent event) {
        onBlockExploded(event.blockList());
    }

    @Override
    public boolean onPlayerInteract(PlayerInteractEvent event, RealmController controller) {
        if (event.getItem() != null && event.getItem().getType().name().toLowerCase().contains("boat")
                && event.getAction().isRightClick()) {
            event.setCancelled(true);
            return false;
        };
        return true;
    }

    @Override
    public boolean onDimensionalTravel(PlayerPortalEvent event, RealmController controller) {
        controller.joinWorld(PlayerUtil.getPlayerData(event.getPlayer()));
        return true;
    }

    @Override
    public void onSet() {
        //Do Nothing on purpose
        BoardMaker boardMaker = realm.getBoardMaker();
        boardMaker.addObserver(this);
        boardMaker.updateAllViewers(this);
    }

    @Override
    public boolean shouldProgressTime() {
        return true;
    }

    @Override
    public void onRemove() {
        realm.getBoardMaker().removeObserver(this);
    }

    @Override
    public void update(BoardMaker maker, Objective board, PlayerData playerData) {
//        Component indent = TextUtil.makeText(" ");
//        maker.editLine(board, 10, indent.append(TextUtil.makeText("Objective: ")));
//        List<Component> objective = getObjectiveName();
//        maker.editLine(board, 9, indent.append(objective.getFirst()));
//        maker.editLine(board, 8, indent.append(objective.size() >= 2 ? objective.get(1) : TextUtil.makeText("")));

        PlayerStats pS = playerData.getPlayerStats();
        maker.editLine(board, 3, TextUtil.makeText(" Realm Kills: " + pS.getNumberStat(REALM_KILLS_KEY)));
        maker.editLine(board, 2, TextUtil.makeText(" Realm Deaths: " + pS.getNumberStat(REALM_DEATHS_KEY)));
    }

    private void onBlockExploded(List<Block> blockList) {
        for (Block block : new ArrayList<>(blockList)) {
            if (!block.hasMetadata(PLACED_BLOCK_TAG)) {
                blockList.remove(block);
                block.removeMetadata(PLACED_BLOCK_TAG, TrablesAdditions.getInstance());
            }
        }
    }


    private Block treatWaterLogging(PlayerBucketEvent event) {
        ItemStack bucketResult = event.getItemStack();
        Block targetBlock = event.getBlock();
        if (targetBlock.getBlockData() instanceof Waterlogged waterlogged) {
            Block relative = event.getBlockClicked().getRelative(event.getBlockFace());
            if (relative.getType() != Material.AIR) {
                int num = event.getPlayer().getEyeHeight() >= targetBlock.getY() ? 1 : -1;
                relative = targetBlock.getLocation().clone().add(0, num,0).getBlock();
            }
            if (relative.getType() == Material.AIR) {
                relative.setType(Material.WATER);
            } else {
                event.setCancelled(true);
                return null;
            }
            event.setCancelled(true);
            if (event.getHand() == EquipmentSlot.HAND) event.getPlayer().getInventory().setItemInMainHand(bucketResult);
            else event.getPlayer().getInventory().setItemInOffHand(bucketResult);
            return relative;
        }
        return targetBlock;
    }
}
