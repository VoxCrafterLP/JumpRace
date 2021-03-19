package com.voxcrafterlp.jumprace.enums;

import com.voxcrafterlp.jumprace.JumpRace;
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

    RED(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("team-name-red"), "§c", Color.RED),
    BLUE(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("team-name-blue"), "§9", Color.BLUE),
    YELLOW(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("team-name-yellow"), "§e", Color.YELLOW),
    GREEN(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("team-name-green"), "§a", Color.LIME),
    PINK(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("team-name-pink"), "§d", Color.fromRGB(247, 17, 228)),
    DARK_GREEN(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("team-name-darkgreen"), "§2", Color.OLIVE),
    LIGHT_BLUE(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("team-name-lightblue"), "§b", Color.AQUA),
    ORANGE(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("team-name-orange"), "§6", Color.ORANGE);

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
