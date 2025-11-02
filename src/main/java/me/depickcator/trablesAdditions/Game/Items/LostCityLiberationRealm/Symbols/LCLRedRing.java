package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.Symbols;

import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.LCLSymbol;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.format.TextColor;

public class LCLRedRing extends LCLSymbol {
    public LCLRedRing() {
        super("Red Ring", "red_ring");
    }

    @Override
    protected String getBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNkMWU5NGJjYWRj" +
                "YjlhMTY1OTc4OTM3Y2UwMzAxMjJmY2NiMDYzNDFmNjkzZjkxYjUzYTU5ZmM4MjUyYWRmYSJ9fX0=";
    }

    @Override
    protected TextColor getColor() {
        return TextUtil.DARK_RED;
    }
}
