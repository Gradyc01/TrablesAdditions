package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.Symbols;

import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.LCLSymbol;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.format.TextColor;

public class LCLCore extends LCLSymbol {
    public LCLCore() {
        super("Core", "core");
    }

    @Override
    protected String getBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTMwODg4MjQ2NDF" +
                "hOGEyN2I1ZGE3MWYyZTAwZDQ4MmMwM2M4NjNjYjBlNTJhMmNmOGQzZTJiNTQ3YWZkYjNiOSJ9fX0=";
    }

    @Override
    protected TextColor getColor() {
        return TextUtil.YELLOW;
    }
}
