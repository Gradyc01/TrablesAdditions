package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Player.PlayerStats;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.AbstractRealmStates;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
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

public abstract class WitherRealmState extends AbstractRealmStates {
    private final WitherRealm realm;
    public WitherRealmState(WitherRealm realm) {
        super(realm);
        this.realm = realm;
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().hasMetadata(WitherRealm.WITHER_REALM_DUNGEON_DISCIPLE_KEY)) {
            String name = event.getEntity().getMetadata(WitherRealm.WITHER_REALM_DUNGEON_DISCIPLE_KEY).getFirst().asString();
            realm.removeDisciple(name);
        }
//        if (event.getDamageSource().getCausingEntity() instanceof Player player) {
//            PlayerData pD = PlayerUtil.getPlayerData(player);
//            pD.getPlayerStats().addNumberStat(REALM_KILLS_KEY, 1);
//            pD.getPlayerScoreboards().updateBoard(this);
//        }
        super.onEntityDeath(event);
    }

    @Override
    public void update(BoardMaker maker, Objective board, PlayerData playerData) {
        Component indent = TextUtil.makeText(" ");
        maker.editLine(board, 10, indent.append(TextUtil.makeText("Objective: ")));
        List<Component> objective = getObjectiveName();
        maker.editLine(board, 9, indent.append(objective.getFirst()));
        maker.editLine(board, 8, indent.append(objective.size() >= 2 ? objective.get(1) : TextUtil.makeText("")));
        super.update(maker, board, playerData);
//        PlayerStats pS = playerData.getPlayerStats();
//        maker.editLine(board, 3, TextUtil.makeText(" Realm Kills: " + pS.getNumberStat(REALM_KILLS_KEY)));
//        maker.editLine(board, 2, TextUtil.makeText(" Realm Deaths: " + pS.getNumberStat(REALM_DEATHS_KEY)));
    }

    /*Returns a List of Components that describe the objective name
    * Only the first two in the list will be used*/
    public abstract List<Component> getObjectiveName();

    public WitherRealm getRealm() {
        return realm;
    }

    @Override
    public String observerName() {
        return "WitherRealmState: " + getStateName();
    }
}
