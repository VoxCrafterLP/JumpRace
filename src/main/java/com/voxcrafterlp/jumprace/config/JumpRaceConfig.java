package com.voxcrafterlp.jumprace.config;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.12.2020
 * Time: 23:31
 * Project: JumpRace
 */

@Getter
public class JumpRaceConfig {

    private final File configFile;
    private final YamlConfiguration configuration;

    private final boolean builderServer;
    private final int moduleSpawnHeight, teamSize, teamAmount, playersRequiredForStart, maxLives;

    private final String adminPermission, builderPermission, setupPermission, mapSwitchPermission;

    public JumpRaceConfig() {
        this.configFile = new File("plugins/JumpRace/config.yml");
        this.configuration = YamlConfiguration.loadConfiguration(this.configFile);

        this.builderServer = this.configuration.getBoolean("builder-server");
        this.moduleSpawnHeight = this.configuration.getInt("module-spawn-height");

        this.teamSize = this.configuration.getInt("team-size");
        this.teamAmount = this.configuration.getInt("team-amount");
        this.playersRequiredForStart = this.configuration.getInt("players-required-for-start");

        this.maxLives = this.configuration.getInt("max-lives");

        this.adminPermission = this.configuration.getString("admin-permission");
        this.builderPermission = this.configuration.getString("builder-permission");
        this.setupPermission = this.configuration.getString("setup-permission");
        this.mapSwitchPermission = this.configuration.getString("switchMap-permission");
    }

}
