package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.Symbols;

import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.LCLSymbol;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.format.TextColor;

public class LCLStars extends LCLSymbol {
    public LCLStars() {
        super("Stars", "stars");
    }

    @Override
    protected String getBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWUyYjAzNGZlZDJ" +
                "jODcxZjc3YjQ3MjQyMWRhNmJlN2ZiYzgyZTFkMzQzNDhkZGNiMDE1MzgwNjFhMWZjM2JiNCJ9fX0=";
    }

    @Override
    protected TextColor getColor() {
        return TextUtil.DARK_GRAY;
    }
}
