package me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.GameStates;

import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.Encounters.LCL_Encounter1;
import me.depickcator.trablesAdditions.Game.Realms.LostCityLiberationRealm.LostCityLiberationRealm;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.Shared.Actions.TeleportToSpawns;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class LCL_E1_Start extends LCL_E1_Skirmish {
    public LCL_E1_Start(LostCityLiberationRealm realm, RealmController controller) {
        super(realm, new LCL_Encounter1(realm, controller));
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        super.onEntityDeath(event);
        if (getEncounter().checkIfInitialEntity(event.getEntity())) nextState();
    }

    @Override
    public void onSet() {
        getEncounter().set();
        super.onSet();
        getEncounter().getAudience().sendMessage(TextUtil.makeText("The Warning Bells Sound", TextUtil.DARK_RED, true, true));
        soundWarningSound();
    }

    @Override
    public boolean onDimensionalTravel(PlayerPortalEvent event, RealmController controller) {
        Player player = event.getPlayer();
        getEncounter().spawnPlayerIn(player);

        return true;
    }

    private void soundWarningSound() {
        Audience audience = getEncounter().getAudience();
        new BukkitRunnable() {
            int times = 10;
            Sound sound = SoundUtil.makeSound(org.bukkit.Sound.BLOCK_BELL_USE, 10, 1);
            Sound sound2 = SoundUtil.makeSound(org.bukkit.Sound.BLOCK_BELL_RESONATE, 10, 1);
            @Override
            public void run() {
                if (times <= 0) cancel();
                audience.playSound(sound);
                audience.playSound(sound2);
                times--;
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 20, 8);
        audience.playSound(SoundUtil.makeSound(org.bukkit.Sound.EVENT_RAID_HORN, 100, 1));
    }
}
