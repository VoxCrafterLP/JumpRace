package com.voxcrafterlp.jumprace.enums;

import lombok.Getter;
import org.bukkit.Color;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 26.02.2021
 * Time: 16:59
 * Project: JumpRace
 */

@Getter
public enum TeamColor {

    RED("Red", "§c", Color.RED),
    BLUE("Blue", "§9", Color.BLUE),
    YELLOW("Yellow", "§e", Color.YELLOW),
    GREEN("Green", "§a", Color.LIME),
    PINK("Pink", "§d", Color.fromRGB(247, 17, 228)),
    DARK_GREEN("DGreen", "§2", Color.OLIVE),
    LIGHT_BLUE("LBlue", "§b", Color.AQUA),
    ORANGE("Orange", "§6", Color.ORANGE);

    private final String displayName;
    private final String colorCode;
    private final Color color;

    TeamColor(String displayName, String colorCode, Color color) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.color = color;
    }

    public String getTeamNameWithColorCode() {
        return this.colorCode + this.displayName;
    }

}
