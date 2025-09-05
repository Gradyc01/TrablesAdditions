package me.depickcator.trablesAdditions.Game.Realms.WitherRealm.UI;

import me.depickcator.trablesAdditions.Game.Player.PlayerData;
import me.depickcator.trablesAdditions.Game.Realms.RealmController;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Action.WitherRealm_BreakDoor;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmEndCrystal;
import me.depickcator.trablesAdditions.Game.Realms.WitherRealm.Mobs.WitherRealmWither;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.UI.Interfaces.TrablesBlockGUI;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

public class WitherRealmStunControlPanel extends WitherRealmControlPanelGUI {
    private final WitherRealmWither wither;
    private final EnderCrystal crystal;
    public WitherRealmStunControlPanel(Block block, WitherRealmWither wither) {
        super(block, TextUtil.makeText("Stun boss", TextUtil.AQUA));
        this.wither = wither;
        this.crystal = (EnderCrystal) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5, 1, 0.5), EntityType.END_CRYSTAL);
        crystal.setShowingBottom(false);
        crystal.setCustomNameVisible(true);
        crystal.setInvulnerable(true);
        crystal.customName(TextUtil.makeText("Stun Boss", TextUtil.YELLOW));
    }

    @Override
    protected void onClickCorrectButton(PlayerData playerData) {
        wither.setTarget(((CraftLivingEntity) playerData.getPlayer()).getHandle());
    }

    @Override
    protected boolean hasReachSuccessfulCompletion() {
        return passKeys >= 12;
    }

    @Override
    protected void resetPassKey() {
        inventory.close();
    }

    @Override
    protected void successfullyCompleted() {
        inventory.close();
        getBlock().setType(Material.AIR);
        TrablesBlockGUI.removeGUI(this);
        stunWither();
    }

    private void stunWither() {
        World world = crystal.getWorld();
        new BukkitRunnable() {
            int ticks = 5 * 20;
            @Override
            public void run() {
                crystal.setBeamTarget(wither.getLocation());
                world.playSound(crystal.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                if (--ticks <= 0) {
                    wither.fullStunWither();
                    crystal.remove();
                    cancel();
                }
            }
        }.runTaskTimer(TrablesAdditions.getInstance(), 0, 1);

    }
}
