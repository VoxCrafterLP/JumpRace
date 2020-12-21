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

    EASY("EASY"),
    NORMAL("NORMAL"),
    HARD("HARD"),
    VERY_HARD("VERY_HARD");

    private String configName;

    ModuleDifficulty(String configName) {
        this.configName = configName;
    }

    public static ModuleDifficulty getModuleDifficultyByConfigName(String configName) {
        for(ModuleDifficulty moduleDifficulty : ModuleDifficulty.values()) {
            if(moduleDifficulty.getConfigName().equals(configName))
                return moduleDifficulty;
        }
        return null;
    }


}
