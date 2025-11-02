package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.Symbols;

import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.LCLSymbol;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.format.TextColor;

public class LCLHalen extends LCLSymbol {
    public LCLHalen() {
        super("Halen", "halen");
    }

    @Override
    protected String getBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmVmYWY4ZDk4ZTE" +
                "wOTgwY2RlYzllNGQxY2RlZDNiMDE1MTFjNDQ1MzU3OWMzOGFiNDZiMGExMDYxYmNiNTdmNyJ9fX0=";
    }

    @Override
    protected TextColor getColor() {
        return TextUtil.WHITE;
    }
}
