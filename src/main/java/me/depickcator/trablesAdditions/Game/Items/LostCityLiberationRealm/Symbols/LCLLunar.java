package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.Symbols;

import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.LCLSymbol;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.format.TextColor;

public class LCLLunar extends LCLSymbol {
    public LCLLunar() {
        super("Lunar", "lunar");
    }

    @Override
    protected String getBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZiZjZhM2I2MGUy" +
                "MDY0MDI1ZTJjNzNkZTJiNzUxOThiNjJkMzU4MmExMDZlZDgzOWI2MDcwNjA4ODY5NmYxNSJ9fX0=";
    }

    @Override
    protected TextColor getColor() {
        return TextUtil.GOLD;
    }
}
