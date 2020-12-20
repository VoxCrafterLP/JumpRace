package com.voxcrafterlp.jumprace;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.12.2020
 * Time: 23:22
 * Project: JumpRace
 */

@Getter
public class JumpRace extends JavaPlugin {

    private static JumpRace instance;

    @Override
    public void onEnable() {
        instance = this;

        this.registerCommands();
        this.registerListeners();

        this.loadConfig();
    }

    private void registerCommands() {

    }

    private void registerListeners() {

    }

    private void loadConfig() {

    }

    public static JumpRace getInstance() { return instance; }

}
