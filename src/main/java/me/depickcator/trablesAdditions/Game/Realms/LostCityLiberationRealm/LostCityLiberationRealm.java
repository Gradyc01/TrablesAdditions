package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.Realm;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmNMSMob;
import me.depickcator.trablesAdditions.Game.Realms.Interfaces.RealmStates;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates.LCL_InitialState;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Sequence.StartGame.StartGameSequence;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Interfaces.BoardMaker;
import me.depickcator.trablesAdditions.Interfaces.ScoreboardObserver;
import me.depickcator.trablesAdditions.Scoreboards.WitherRealmBoard;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

import java.util.Map;

public class LostCityLiberationRealm extends Realm implements ScoreboardObserver {
    private int timeTicks;
    private LCL_Encounter encounter;

    public LostCityLiberationRealm(Location portalLocation) {
        super(portalLocation, "lost_city_liberation", "Lost City: Liberation");
        timeTicks = 0;
        encounter = null;
    }

    @Override
    public void initialize(PlayerData playerData) {
        if (getPortalLocation().getBlock().isLiquid()) return;
        setRealmState(getStartingRealmState());
        Player player = playerData.getPlayer();
        TextUtil.broadcastMessage(TextUtil.makeText(player.getName() + " has started a " + getDisplayName(), TextUtil.AQUA));
        controller = new RealmController(this);
        controller.initialize(); //Don't add anything past this that relies on the controller as the root may still be null
    }

    private void timeTick() {
        if (getRealmState().shouldProgressTime() && timeTicks++ % 4 == 0) {
            getBoardMaker().updateAllViewers(this);
        }
    }

    @Override
    public void onStart(RealmController controller) {
        new StartGameSequence(this, controller).start();
    }

    @Override
    public void onLoop(RealmController controller) {
        timeTick();
        if (encounter != null) encounter.onLoop();
    }

    @Override
    public void onEnd(RealmController controller) {

    }

    @Override
    protected RealmStates getStartingRealmState() {
        return new LCL_InitialState(this);
    }

    @Override
    public void worldRules(World world) {
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        world.setGameRule(GameRule.WATER_SOURCE_CONVERSION, false);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
    }

    @Override
    public void onStartBoss(RealmController controller) {

    }

    @Override
    public void onBossDefeated(RealmController controller) {

    }

    @Override
    public BoardMaker getBoardMaker() {
        return WitherRealmBoard.getInstance();
    }

    @Override
    public Map<Material, Integer> getUnFloodables() {
        return Map.of(
                Material.ANDESITE, 20,
                Material.MOSSY_COBBLESTONE, 20,
                Material.GRANITE, 20,
                Material.STONE_BRICKS, 20,
                Material.BRICKS, 10,
                Material.MOSSY_STONE_BRICKS, 10,
                Material.AIR, 0);
    }

    @Override
    public void update(BoardMaker maker, Objective board, PlayerData playerData) {
        maker.editLine(board, 13, TextUtil.makeText(" Time Elapsed:"));
        maker.editLine(board, 12, TextUtil.makeText("   " + TextUtil.formatTime(timeTicks/4), TextUtil.AQUA));
    }

    @Override
    public String observerName() {
        return "Lost City: Liberation";
    }

    public LCL_Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(LCL_Encounter encounter) {
        TextUtil.debugText("LCL Realm", "Set the encounter from " +
                (this.encounter == null ? "null" : this.encounter.getName()) + "to" +
                (encounter == null ? "null" : encounter.getName()));
        this.encounter = encounter;
    }
}
