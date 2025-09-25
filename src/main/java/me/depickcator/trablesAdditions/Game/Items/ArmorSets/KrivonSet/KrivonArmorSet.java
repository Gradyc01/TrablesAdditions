package me.depickcator.trablesAdditions.Game.Items.ArmorSets.KrivonSet;

import me.depickcator.trablesAdditions.Game.Items.Interfaces.ArmorSet;
import me.depickcator.trablesAdditions.Game.Player.PlayerArmorEffects;
import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.SoundUtil;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public class KrivonArmorSet extends ArmorSet {
    private static KrivonArmorSet instance;
    private final AttributeModifier modifier;
    private KrivonArmorSet() {
        super("Krivon");
        modifier = new AttributeModifier(new NamespacedKey(TrablesAdditions.getInstance(), getName() + "_health_text"),
                0.25, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    }

    @Override
    protected void removeFullArmorSetBonus(PlayerArmorEffects effects) {
        TextUtil.debugText("KrivonArmorSet", "Full set removed");
//        Player player = effects.getPlayerData().getPlayer();
//        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
//        if (attribute != null) attribute.removeModifier(modifier);
    }

    @Override
    protected void addFullArmorSetBonus(PlayerArmorEffects effects) {
        TextUtil.debugText("KrivonArmorSet", "Full set added");
//        Player player = effects.getPlayerData().getPlayer();
//        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
//        try {
//            if (attribute != null) attribute.addModifier(modifier);
//        } catch (IllegalArgumentException e) {
//            TextUtil.debugText("KrivonArmorSet", "Full set already added " + e.getMessage());
//        }
    }

    @Override
    public void triggerTemporaryDamageArmorSetEffects(PlayerArmorEffects effects, EntityDamageByEntityEvent event) {
        String key = "KRIVON_FINAL_STAND";
        super.triggerTemporaryDamageArmorSetEffects(effects, event);
        Player player = effects.getPlayerData().getPlayer();
        Instant now = Instant.now();
        if (player.hasMetadata(key)) {
            Instant time = Instant.parse(player.getMetadata(key).getFirst().asString());
            if (!now.isAfter(time.plusSeconds(30))) return;
        }
//        if (player.getMetadata(key).getFirst().asString())
        double playerHealth = player.getHealth() - event.getFinalDamage();
        TextUtil.debugText(playerHealth + "");
        if (playerHealth <= player.getAttribute(Attribute.MAX_HEALTH).getValue() * 0.2 && playerHealth > 0) {
            player.setMetadata(key, new FixedMetadataValue(TrablesAdditions.getInstance(), now));
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 10 * 20, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5, 0));
            player.playSound(SoundUtil.makeSound(Sound.ENTITY_SKELETON_HORSE_DEATH, 10, 1));
        player.sendMessage(TextUtil.makeText("[Krivon Set]", TextUtil.YELLOW).append(TextUtil.makeText(" Your final stand has been triggered", TextUtil.AQUA)));
            player.getWorld().createExplosion(player, player.getLocation(), 6, false, false, true);
        }
    }

    @Override
    public List<Component> getArmorSetBonusLore() {
        return List.of(
                TextUtil.makeText("Full Set Bonus: When near death,", TextUtil.GRAY),
                TextUtil.makeText("Triggers an explosion killing ", TextUtil.GRAY),
                TextUtil.makeText("everything nearby", TextUtil.GRAY)
        );
    }

    public static KrivonArmorSet getInstance() {
        if (instance == null) instance = new KrivonArmorSet();
        return instance;
    }
}
