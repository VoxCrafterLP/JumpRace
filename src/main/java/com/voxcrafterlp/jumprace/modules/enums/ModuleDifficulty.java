package com.voxcrafterlp.jumprace.modules.enums;

import lombok.Getter;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 00:19
 * Project: JumpRace
 */

@Getter
public enum ModuleDifficulty {

    EASY("EASY", "§aEasy"),
    NORMAL("NORMAL", "§6Normal"),
    HARD("HARD", "§cHard"),
    VERY_HARD("VERY_HARD", "§4Very hard");

    private final String configName;
    private final String displayName;

    ModuleDifficulty(String configName, String displayName) {
        this.configName = configName;
        this.displayName = displayName;
    }

    public static ModuleDifficulty getModuleDifficultyByConfigName(String configName) {
        for(ModuleDifficulty moduleDifficulty : ModuleDifficulty.values()) {
            if(moduleDifficulty.getConfigName().equals(configName))
                return moduleDifficulty;
        }
        return null;
    }
}
