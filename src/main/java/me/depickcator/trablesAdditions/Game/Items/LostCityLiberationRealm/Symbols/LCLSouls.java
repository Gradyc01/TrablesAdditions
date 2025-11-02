package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.Symbols;

import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.LCLSymbol;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.format.TextColor;

public class LCLSouls extends LCLSymbol {
    public LCLSouls() {
        super("Souls", "souls");
    }

    @Override
    protected String getBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWUxN2I1ZWE0ZTI4Y" +
                "jMyNDUxNGZkZmQ2ZTkyYzY3MGI1MzRiM2JmMDg0NzUzYjkwZjVlNjhiOGU5NmUwYzJkYSJ9fX0=";
    }

    @Override
    protected TextColor getColor() {
        return TextUtil.BLUE;
    }
}
