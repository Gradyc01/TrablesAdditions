package me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.Symbols;

import me.depickcator.trablesAdditions.Game.Items.LostCityLiberationRealm.LCLSymbol;
import me.depickcator.trablesAdditions.Util.TextUtil;
import net.kyori.adventure.text.format.TextColor;

public class LCLFragment extends LCLSymbol {
    public LCLFragment() {
        super("Fragment", "fragment");
    }

    @Override
    protected String getBase64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFlYzI5MmIzYWE2" +
                "NjVkNjAwMWE5OTgzMTM5NjYyMTJlYWM1NDE3ZWE5MGY4M2JhYTQ2YjQ4YTk5NDI2OThmMyJ9fX0=";
    }

    @Override
    protected TextColor getColor() {
        return TextUtil.RED;
    }
}
