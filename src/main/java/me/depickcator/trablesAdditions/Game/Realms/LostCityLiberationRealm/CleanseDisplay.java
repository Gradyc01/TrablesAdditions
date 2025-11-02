package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm;

import me.depickcator.trablesAdditions.Game.Player.PlayerStates.TrueSightState;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Entities.ItemDisplay;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Entities.ItemDisplayDetection;
import me.depickcator.trablesAdditions.Util.PlayerUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CleanseDisplay extends ItemDisplayDetection {
    private final LCL_Encounter encounter;
    private final boolean fakeCleanse;
    private final Location loc;
    public CleanseDisplay(Location location, LCL_Encounter encounter, boolean fakeCleanse) {
        super(location, new ItemStack(Material.AIR));
        this.encounter = encounter;
        encounter.addCleanseDisplay(this);
        this.fakeCleanse = fakeCleanse;
        this.loc = location;
        this.setCustomNameVisible(false);
    }

    @Override
    public void tick() {
        super.tick();
        World world = loc.getWorld();
        if (this.isAlive()) {
            world.spawnParticle(Particle.EFFECT, loc, 5, 1, 1, 1, new Particle.Spell(Color.WHITE, 2));
        }
    }

    @Override
    protected void uponPlayerWalkIn(Player player) {
        if (!(PlayerUtil.getPlayerData(player).getPlayerState() instanceof TrueSightState)) {
            player.damage(0.5);
        } else if (fakeCleanse) {
            if (player.getGameMode() == GameMode.SURVIVAL) player.setHealth(0.0);
        } else {
            encounter.cleanseTrueSight(player);
        }
    }
}
