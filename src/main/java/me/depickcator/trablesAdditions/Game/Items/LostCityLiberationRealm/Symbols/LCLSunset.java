package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.Symbols;

import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.LCLSymbol;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.format.TextColor;

public class LCLSunset extends LCLSymbol {
    public LCLSunset() {
        super("Sunset", "sunset");
    }

    @Override
    protected String getBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM5MWUyODBhN2Y" +
                "0YzgzMzQ4MTU1YWJjYzRiNjhjMzRkY2Y0YzI0MjIzZjY4MTRiZjQyMjc4ZDk3ZWQ2ODFmNSJ9fX0=";
    }

    @Override
    protected TextColor getColor() {
        return TextUtil.PINK;
    }
}
