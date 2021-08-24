package fr.obelouix.essentials.utils;

import net.kyori.adventure.text.format.TextColor;

public enum TextColors {

    BLACK(TextColor.color(0, 0, 0)),
    DARK_BLUE(TextColor.color(0, 0, 170)),
    DARK_GREEN(TextColor.color(0, 170, 0)),
    DARK_AQUA(TextColor.color(0, 170, 170)),
    DARK_PURPLE(TextColor.color(170, 0, 170)),
    DARK_RED(TextColor.color(170, 0, 0)),
    GOLD(TextColor.color(255, 170, 0)),
    GRAY(TextColor.color(170, 170, 170)),
    DARK_GRAY(TextColor.color(85, 85, 85)),
    BLUE(TextColor.color(85, 85, 255)),
    GREEN(TextColor.color(85, 255, 85)),
    AQUA(TextColor.color(85, 255, 255)),
    RED(TextColor.color(255, 85, 85)),
    LIGHT_PURPLE(TextColor.color(255, 85, 255)),
    YELLOW(TextColor.color(255, 255, 85)),
    WHITE(TextColor.color(0, 0, 0));

    private final TextColor textColor;

    TextColors(TextColor color) {
        this.textColor = color;
    }

    public TextColor getTextColor() {
        return textColor;
    }
}
