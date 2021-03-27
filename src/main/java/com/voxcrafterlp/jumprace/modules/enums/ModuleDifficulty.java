package com.voxcrafterlp.jumprace.modules.enums;

import com.voxcrafterlp.jumprace.JumpRace;
import lombok.Getter;

import java.util.Arrays;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 00:19
 * Project: JumpRace
 */

@Getter
public enum ModuleDifficulty {

    EASY("EASY", JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-difficulty-easy")),
    NORMAL("NORMAL", JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-difficulty-normal")),
    HARD("HARD", JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-difficulty-hard")),
    VERY_HARD("VERY_HARD", JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-difficulty-veryhard"));

    private final String configName;
    private final String displayName;

    ModuleDifficulty(String configName, String displayName) {
        this.configName = configName;
        this.displayName = displayName;
    }

    public static ModuleDifficulty getModuleDifficultyByConfigName(String configName) {
        return Arrays.stream(ModuleDifficulty.values()).filter(moduleDifficulty -> moduleDifficulty.getConfigName().equals(configName)).findAny().get();
    }
}
