package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.Symbols;

import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.LCLSymbol;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.format.TextColor;

public class LCLPortal extends LCLSymbol {
    public LCLPortal() {
        super("Portal", "portal");
    }

    @Override
    protected String getBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjdiNmY1YjBkNzEw" +
                "ZTU5YTk5YmM4MWY1NTE0NTg1ZTYxYjU0N2JlNGQzYjgyY2M1ZTBkODYyZjE0YTYyNTc0ZSJ9fX0=";
    }

    @Override
    protected TextColor getColor() {
        return TextUtil.DARK_PURPLE;
    }
}
